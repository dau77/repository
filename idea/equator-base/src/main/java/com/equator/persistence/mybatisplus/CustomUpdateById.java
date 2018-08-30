package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * UpdateById，增加简单联合主键支持
 */
public class CustomUpdateById extends UpdateById {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if(SqlInjectorUtils.isExistedSourceField(tableInfo)) {
            SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID; //<script>UPDATE %s %s WHERE %s=#{%s} %s</script>

            String sql = sqlMethod.getSql().replace("WHERE", "WHERE %s=#{%s} and");
            sql = String.format(sql, tableInfo.getTableName(),
                    sqlSet(false, false, tableInfo, Constants.ENTITY_SPOT),
                    tableInfo.getKeyColumn(), "et." + tableInfo.getKeyProperty(), "source", "et.source",
                    new StringBuilder("<if test=\"et instanceof java.util.Map\">")
                            .append("<if test=\"et.MP_OPTLOCK_VERSION_ORIGINAL!=null\">")
                            .append(" AND ${et.MP_OPTLOCK_VERSION_COLUMN}=#{et.MP_OPTLOCK_VERSION_ORIGINAL}") //乐观锁？
                            .append("</if></if>"));
            //因为MP不认为source是主键，所以sqlSet()方法会产生source的相关脚本，需要自己删除
            //注意：TableInfoHelper.initTableInfo，好像处理fieldList时不会包含主键，如果联合主键标了2个，有可能会有问题
            sql = sql.replace("<if test=\"et.source!=null\">source=#{et.source},</if>", "");

            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
        }
        else {
            return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
        }
    }
}

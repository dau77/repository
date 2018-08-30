package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * DeleteById，增加简单联合主键支持
 */
public class CustomDeleteById extends DeleteById {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if(SqlInjectorUtils.isExistedSourceField(tableInfo)) {
            SqlMethod sqlMethod = SqlMethod.DELETE_BY_ID;
            String sql = sqlMethod.getSql().replace("</script>", " and %s=#{%s}</script>");

            SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(sql,
                    tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(), "source", "source"), modelClass);
            return this.addDeleteMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource);
        }
        else {
            return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
        }







    }
}

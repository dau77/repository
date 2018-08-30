package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 继承DeleteBatchByIds，增加简单联合主键支持
 */
public class CustomDeleteBatchByIds extends DeleteBatchByIds {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if(SqlInjectorUtils.isExistedSourceField(tableInfo)) {
            SqlMethod sqlMethod = SqlMethod.DELETE_BATCH_BY_IDS; //<script>DELETE FROM %s WHERE %s IN (%s)</script>
            String sql = String.format("<script>DELETE FROM %s WHERE (%s, source) IN (%s)</script>", tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    "<foreach item=\"item\" collection=\"coll\" separator=\",\">(#{item." + tableInfo.getKeyProperty() + "},#{item.source})</foreach>");
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addDeleteMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource);
        }
        else {
            return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
        }
    }
}

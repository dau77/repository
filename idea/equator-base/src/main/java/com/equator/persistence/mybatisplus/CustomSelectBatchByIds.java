package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.SelectBatchByIds;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * SelectBatchByIds，增加简单联合主键支持
 */
public class CustomSelectBatchByIds extends SelectBatchByIds {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if(SqlInjectorUtils.isExistedSourceField(tableInfo)) {
            SqlMethod sqlMethod = SqlMethod.SELECT_BATCH_BY_IDS;

            String sql = "<script>SELECT %s FROM %s WHERE (%s, source) IN (%s)</script>";

            SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(sql,
                    sqlSelectColumns(tableInfo, false), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    new StringBuilder("<foreach item=\"item\" collection=\"coll\" separator=\",\">(#{item." + tableInfo.getKeyProperty() + "},#{item.source})</foreach>").toString()),
                    modelClass);
            return addSelectMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource, modelClass, tableInfo);
        }
        else {
            return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
        }
    }
}

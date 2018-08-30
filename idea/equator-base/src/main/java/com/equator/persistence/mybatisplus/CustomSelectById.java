package com.equator.persistence.mybatisplus;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

import java.util.List;

/**
 * SelectById，增加简单联合主键支持
 */
public class CustomSelectById extends SelectById {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if(SqlInjectorUtils.isExistedSourceField(tableInfo)) {
//        if("userinfo".equals(tableInfo.getTableName())) {
            //方案一：如果只用于兼容旧的代码,判断是否有source字段特别处理即可
            //方案二：研究是否可以用类似XML那类，可以使用条件判断的SQL语句.类SelectByMap? 感觉不太好
            //方案三：使用自定义注解等，标注其它的主键，通过modelClass可以取到（另外要考虑是否将主键拆分为父类）

            SqlMethod sqlMethod = SqlMethod.SELECT_BY_ID;
            String sql = sqlMethod.getSql() + " and %s=#{%s}";
            SqlSource sqlSource = new RawSqlSource(configuration, String.format(sql, this.sqlSelectColumns(tableInfo, false),
                    tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(), "source", "source"), Object.class);
            return this.addSelectMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource, modelClass, tableInfo);
        }
        else {
            return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
        }
    }
}


package com.equator.dao.handler;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedJdbcTypes;
//import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * 自定义类型转换（JAVA的String -> 数据库的integer）
 */
//@MappedTypes({String.class})
//@MappedJdbcTypes({JdbcType.INTEGER})
public class CustomStringToIntegerTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, NumberUtils.toInt(parameter, 0));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseString(rs.getInt(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseString(rs.getInt(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseString(cs.getInt(columnIndex));
    }

    private String parseString(int columnValue) {
        return String.valueOf(columnValue);
    }
}

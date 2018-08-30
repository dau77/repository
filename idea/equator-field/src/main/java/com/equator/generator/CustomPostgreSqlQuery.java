package com.equator.generator;

import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;

/**
 * PostgreSql 表数据查询相关
 * 继承重写tableFieldsSql()方法，可以获取联合主键字段
 */
public class CustomPostgreSqlQuery extends PostgreSqlQuery {

    @Override
    public String tableFieldsSql() {
        //重写改为可以取联合主键
        return "SELECT A.attname AS name, format_type(A.atttypid, A.atttypmod) AS type,col_description(A.attrelid, A.attnum) AS comment, (CASE C.contype WHEN 'p' THEN 'PRI' ELSE '' END) AS key " +
                "FROM pg_attribute A LEFT JOIN pg_constraint C ON A.attnum = any(C.conkey) AND A.attrelid = C.conrelid " +
                "WHERE  A.attrelid = '%s.%s'::regclass AND A.attnum > 0 AND NOT A.attisdropped ORDER  BY A.attnum";


        //A.attnum = C.conkey[1]改为 A.attnum = any(C.conkey)          --conkey是数组
//        SELECT A.attnum, A.attname AS name, format_type(A.atttypid, A.atttypmod) AS type,col_description(A.attrelid, A.attnum) AS comment,
//        (CASE C.contype WHEN 'p' THEN 'PRI' ELSE '' END) AS key, C.contype, C.conrelid
//
//        FROM pg_attribute A LEFT JOIN pg_constraint C ON A.attnum = any(C.conkey) AND A.attrelid = C.conrelid
//        WHERE  A.attrelid = 'public.order_info'::regclass AND A.attnum > 0 AND NOT A.attisdropped
//        ORDER  BY A.attnum
    }
}

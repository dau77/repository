package com.equator.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.IDbQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * MP代码生成器数据库配置
 */
@Data //如果不写这个注解，在调用setDbType(DbType.POSTGRE_SQL)后，dbQueryFlag会变为true，原因未知
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomDataSourceConfig extends DataSourceConfig {

    /**
     * 数据库信息查询初始化标记()
     */
    private boolean dbQueryFlag = false;


    /**
     * 重写方法，当DB是PostgreSQL时，获取IDbQuery时改用自定义的类（查询主键时，加入联合主键的SQL支持）
     * @return
     */
    @Override
    public IDbQuery getDbQuery() {
        if(!dbQueryFlag && DbType.POSTGRE_SQL == getDbType()) {
            IDbQuery dbQuery = new CustomPostgreSqlQuery();
            this.setDbQuery(dbQuery);
            this.dbQueryFlag = true;
            return dbQuery;
        }
        else {
            return super.getDbQuery();
        }
    }
}

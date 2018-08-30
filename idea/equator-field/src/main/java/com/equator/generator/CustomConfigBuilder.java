package com.equator.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomConfigBuilder extends ConfigBuilder {

    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private IDbQuery dbQuery;

    /**
     * <p>
     * 在构造器中处理配置
     * </p>
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public CustomConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                               TemplateConfig template, GlobalConfig globalConfig) {
        super(packageConfig, dataSourceConfig, strategyConfig, template, globalConfig);
        //覆盖/替换父类产生的数据
        initCustomInfo(packageConfig, dataSourceConfig, strategyConfig, template, globalConfig);
    }

    private void initCustomInfo(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                                TemplateConfig template, GlobalConfig globalConfig) {
        dbQuery = dataSourceConfig.getDbQuery();
        connection = dataSourceConfig.getConn();

        try {
            List<TableInfo> tableInfoList = getTableInfoList();
            List<TableInfo> newList = new ArrayList<>(tableInfoList.size());
            for(TableInfo tableInfo : tableInfoList) {
                //将原始TableInfo的属性复制到自定义类中
                CustomTableInfo newInfo = new CustomTableInfo(tableInfo, strategyConfig);
                //处理联合主键数据
                handlerKeyFields(newInfo, dataSourceConfig);
//                //处理import包
//                handlerImportPackages(tableInfo, newInfo);

                newList.add(newInfo);
            }
            //替换原TableInfoList
            this.setTableInfoList(newList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    private void handlerImportPackages(TableInfo tableInfo, CustomTableInfo newTableInfo) {
//        Set<String> importPackages = newTableInfo.getImportPackages();
//
//        if(newTableInfo.getKeyFields().size() < 2) {
//            if(!importPackages.contains(TableId.class.getCanonicalName())) { //补@TableId
//                importPackages.add(TableId.class.getCanonicalName());
//            }
//            return;
//        }
//
//        //存在联合主键
//        newTableInfo.addKeyImportPackages(TableId.class.getCanonicalName());
//        if(importPackages.contains(IdType.class.getCanonicalName())) {
//            newTableInfo.addKeyImportPackages(IdType.class.getCanonicalName());
//        }
//
//        if (StringUtils.isNotEmpty(getStrategyConfig().getSuperEntityClass())) {
//            importPackages.remove(getStrategyConfig().getSuperEntityClass());
//            newTableInfo.addKeyImportPackages(getStrategyConfig().getSuperEntityClass());
//        }
//
//        //如果是联合主键，会有主键类，所以把主键相关import移除
//        importPackages.remove(TableId.class.getCanonicalName());
//        importPackages.remove(IdType.class.getCanonicalName());
//        //
//        Set<String> keyFields = newTableInfo.getKeyFields();
//        List<TableField> fieldList = tableInfo.getFields();
//        for(TableField field : fieldList) {
//            if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
//                String pkg = field.getColumnType().getPkg();
//                if(keyFields.contains(field.getName())) {
//                    newTableInfo.addKeyImportPackages(pkg);
//                }
//                else {
////                    importPackages.add(pkg);
//                }
//            }
//        }
//    }

    /**
     * <p>
     * 处理联合主键数据。参考父类方法
     * </p>
     *
     * @param tableInfo 表信息
     * @param dataSourceConfig
     * @return
     */
    private void handlerKeyFields(CustomTableInfo tableInfo, DataSourceConfig dataSourceConfig) {
        PreparedStatement preparedStatement = null;

        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            if (DbType.POSTGRE_SQL == dbQuery.dbType()) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaname(), tableInfo.getName());
            } else if (DbType.ORACLE == dbQuery.dbType()) {
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", dataSourceConfig.getSchemaname()), tableInfo.getName());
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableInfo.getName());
            }
            preparedStatement = connection.prepareStatement(tableFieldsSql);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                String key = results.getString(dbQuery.fieldKey());
                boolean isId;
                if (DbType.DB2 == dbQuery.dbType()) {
                    isId = StringUtils.isNotEmpty(key) && "1".equals(key);
                } else {
                    isId = StringUtils.isNotEmpty(key) && "PRI".equals(key.toUpperCase());
                }
                // 将联合主键都保存到CustomTableInfo
                if(isId) {
                    tableInfo.addKeyField(results.getString(dbQuery.fieldName()));
                }

            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        } finally {
            // 释放资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
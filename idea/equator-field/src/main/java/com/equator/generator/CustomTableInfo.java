package com.equator.generator;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 在原来的基础上增加保存联合主键
 */
public class CustomTableInfo extends TableInfo {

    private Set<String> keyFields = new HashSet<>();

    private Set<String> keyImportPackages = new HashSet<>();

    public CustomTableInfo() {

    }

    /**
     * 将原始TableInfo的属性复制到自定义类中
     * @param tableInfo
     * @param strategyConfig
     */
    public CustomTableInfo(TableInfo tableInfo, StrategyConfig strategyConfig) {
        BeanUtils.copyProperties(tableInfo, this);
        //
        setEntityName(strategyConfig, tableInfo.getEntityName());
        //import包原原来不同，后继还要处理
        Set<String> packages  = tableInfo.getImportPackages();
        for(String pkg : packages) {
            this.setImportPackages(pkg);
        }
    }

    /**
     * 返回主键字段名的SET
     * @return
     */
    public Set<String> getKeyFields() {
        return this.keyFields;
    }

    public void addKeyField(String keyField) {
//        this.keyFields.contains()
        this.keyFields.add(keyField);
    }

    /**
     * 有联合主键才有效
     * @return
     */
    public Set<String> getKeyImportPackages() {
        return keyImportPackages;
    }

    public void addKeyImportPackages(String name) {
        keyImportPackages.add(name);
    }
}

package com.equator.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class CustomAutoGenerator extends AutoGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CustomAutoGenerator.class);


    private FrontEndConfigBuilder frontEndConfigBuilder; //用于初始化和保存Field数据

    /**
     * 生成代码
     */
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置。使用CustomConfigBuilder替换ConfigBuilder
        if (null == config) {
            config = new CustomConfigBuilder(getPackageInfo(), getDataSource(), getStrategy(), getTemplate(), getGlobalConfig());
            if (null != injectionConfig) {
                injectionConfig.setConfig(config);
            }
        }
        //
//        super.execute();

        if (null == getTemplateEngine()) {
            setTemplateEngine(new CustomFreemarkerTemplateEngine());
        }
        // 模板引擎初始化执行文件输出
        AbstractTemplateEngine engine = getTemplateEngine().init(this.pretreatmentConfigBuilder(config));
        if(this.frontEndConfigBuilder != null && CustomFreemarkerTemplateEngine.class.isInstance(engine)) {
            //TODO 前端数据FieldConfig初始化？到Engine ?
//            frontEndConfigBuilder = new FrontEndConfigBuilder(pageNames);
            ((CustomFreemarkerTemplateEngine) engine).setFrontEndConfigBuilder(this.frontEndConfigBuilder);
        }
        engine.mkdirs().batchOutput().open();
        logger.debug("==========================文件生成完成！！！==========================");
    }

    /**
     * <p>
     * 预处理配置
     * </p>
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        ConfigBuilder cfg = super.pretreatmentConfigBuilder(config);

        List<TableInfo> tableList = this.getAllTableInfoList(cfg);
        for (TableInfo tableInfo : tableList) {
            //处理import包
            handlerImportPackages(cfg, (CustomTableInfo) tableInfo);
        }
        return cfg.setTableInfoList(tableList);
    }

    /**
     * 处理import包
     * @param config
     * @param newTableInfo
     */
    private void handlerImportPackages(ConfigBuilder config, CustomTableInfo newTableInfo) {
        Set<String> importPackages = newTableInfo.getImportPackages();

        if(newTableInfo.getKeyFields().size() < 2) {
            if(!importPackages.contains(TableId.class.getCanonicalName())) { //补@TableId
                importPackages.add(TableId.class.getCanonicalName());
            }
            return;
        }

        //存在联合主键
        newTableInfo.addKeyImportPackages(TableId.class.getCanonicalName());
        if(importPackages.contains(IdType.class.getCanonicalName())) {
            newTableInfo.addKeyImportPackages(IdType.class.getCanonicalName());
        }

        if (StringUtils.isNotEmpty(config.getStrategyConfig().getSuperEntityClass())) {
            importPackages.remove(config.getStrategyConfig().getSuperEntityClass());
            newTableInfo.addKeyImportPackages(config.getStrategyConfig().getSuperEntityClass());
        }

        //如果是联合主键，会有主键类，所以把主键相关import移除
        importPackages.remove(TableId.class.getCanonicalName());
        importPackages.remove(IdType.class.getCanonicalName());
        //
        Set<String> keyFields = newTableInfo.getKeyFields();
        List<TableField> fieldList = newTableInfo.getFields();
        for(TableField field : fieldList) {
            if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                String pkg = field.getColumnType().getPkg();
                if(keyFields.contains(field.getName())) {
                    newTableInfo.addKeyImportPackages(pkg);
                }
                else {
//                    importPackages.add(pkg);
                }
            }
        }
    }

    public FrontEndConfigBuilder getFrontEndConfigBuilder() {
        return frontEndConfigBuilder;
    }

    public CustomAutoGenerator setFrontEndConfigBuilder(FrontEndConfigBuilder frontEndConfigBuilder) {
        this.frontEndConfigBuilder = frontEndConfigBuilder;
        return this;
    }
}

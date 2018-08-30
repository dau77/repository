package com.equator.generator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.equator.exception.ErrorException;
import com.equator.model.Field;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFreemarkerTemplateEngine extends AbstractTemplateEngine {

    /**
     * 配置信息
     */
    private FrontEndConfigBuilder frontEndConfigBuilder;

    private List<String> frontEndCreatedList = new ArrayList<>();

    private Configuration configuration;

    @Override
    public CustomFreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        configuration = new Configuration();
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(CustomFreemarkerTemplateEngine.class, StringPool.SLASH);
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        fileOutputStream.close();
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        StringBuilder fp = new StringBuilder();
        fp.append(filePath).append(".ftl");
        return fp.toString();
    }

    //-------------------------------

    /**
     * 输出到字符串
     * @param objectMap
     * @param templatePath
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String writerString(Map<String, Object> objectMap, String templatePath) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath);
        Writer out = new StringWriter();
        template.process(objectMap, out);
        return out.toString();
    }


    /**
     * <p>
     * 设置前端配置
     * </p>
     */
    public CustomFreemarkerTemplateEngine setFrontEndConfigBuilder(FrontEndConfigBuilder frontEndConfigBuilder) {
        this.frontEndConfigBuilder = frontEndConfigBuilder;
        return this;
    }


    /**
     * <p>
     * 输出
     * </p>
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
//                    injectionConfig.initMap(); 在pretreatmentConfigBuilder()已经调用
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(foc.outputFile(tableInfo))) {
                                writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                //如果有设置后端输出目录，则按后端输出规则输出
                if(isGenBackEnd()) {
                    genBackEnd(tableInfo, objectMap, pathInfo, template);
                }
                //如果有设置前端输出目录，则按前端输出规则输出
                if(isGenFrontEnd()) {
                    genFrontEnd(tableInfo, objectMap, pathInfo);
                    //

                }






                //SQL
                //TODO


            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
            throw new ErrorException("generator.error");
        }

        return this;
    }

    private void genFrontEnd(TableInfo tableInfo, Map<String, Object> baseObjectMap, Map<String, String> pathInfo) throws Exception {
        Map<String, List<Field>> fieldsMap = frontEndConfigBuilder.getFieldsMap(tableInfo.getName());
        if(fieldsMap == null) {
            return;
        }

        String ouputPath = pathInfo.get(GenConsts.FRONT_END_PATH); //frontEndConfigBuilder.getOutputDir();
        for(Map.Entry<String, List<Field>> entry : fieldsMap.entrySet()) {
            Map<String, Object> objectMap = getObjectMap(tableInfo, baseObjectMap, entry);

            String file = ouputPath + frontEndConfigBuilder.getPageFile(entry.getKey()) + ".vue";
            if(isCreate(file)) {
                String templatePath = "/templates/" + entry.getKey() + ".vue.ftl";
                writer(objectMap, templatePath, file);

                frontEndCreatedList.add(entry.getKey());

                if(isGenFrontEndMenu()) {
                    genFrontEndAppend(objectMap, pathInfo.get(GenConsts.MENU_PATH));
                }
                //
                if(isGenFrontEndRouter()) {
                    genFrontEndAppend(objectMap, pathInfo.get(GenConsts.ROUTER_PATH));
                }
            }

        }
    }

    private void genFrontEndAppend(Map<String, Object> objectMap, String path) throws Exception {
        File file = new File(path);
        File tmp = new File(path + ".tmp");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
        ) {
            String str;
            while ((str = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("generator_([ba]):([a-zA-Z_]+)");
                Matcher m = pattern.matcher(str);
                boolean find = m.find() && m.groupCount() == 2;

                if(find && "b".equals(m.group(1))) {
                    writeAppend(writer, objectMap, m.group(2));
                }

                writeLine(writer, str);

                if(find && "a".equals(m.group(1))) {
                    writeAppend(writer, objectMap, m.group(2));
                }
            }

        } catch (IOException e) {
            throw e;
        }

        //替换原文件，并删除临时文件
        Files.copy(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(tmp.toPath());
    }

    private void writeAppend(BufferedWriter writer, Map<String, Object> objectMap, String templateName) throws IOException, TemplateException {
        String templatePath = String.format("/templates/%s.vue.ftl", templateName); //"/templates/" + entry.getKey() + ".vue.ftl";
        String content = writerString(objectMap, templatePath);
        writeLine(writer, content);
    }

    private static void writeLine(BufferedWriter writer, String str) throws IOException {
        writer.write(str);
        writer.newLine();
    }

    private void genBackEnd(TableInfo tableInfo, Map<String, Object> objectMap, Map<String, String> pathInfo,
                            TemplateConfig template) throws Exception {

        // Mp.java
        String entityName = tableInfo.getEntityName();
        if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
            String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
            if (isCreate(entityFile)) {
                writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
            }
        }
        // MpMapper.java
        if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
            String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
            if (isCreate(mapperFile)) {
                writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
            }
        }
        // MpMapper.xml
        if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
            String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
            if (isCreate(xmlFile)) {
                writer(objectMap, templateFilePath(template.getXml()), xmlFile);
            }
        }
        // IMpService.java
        if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
            String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
            if (isCreate(serviceFile)) {
                writer(objectMap, templateFilePath(template.getService()), serviceFile);
            }
        }
        // MpServiceImpl.java
        if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
            String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
            if (isCreate(implFile)) {
                writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
            }
        }
        // MpController.java
        if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
            String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
            if (isCreate(controllerFile)) {
                writer(objectMap, templateFilePath(template.getController()), controllerFile);
            }
        }
        //主键类
        CustomTableInfo cTableInfo = (CustomTableInfo) tableInfo;
        if(cTableInfo.getKeyFields().size() > 1) { //单主键,不需要处理
            if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%sKey" + suffixJavaOrKt()), entityName);
                if (isCreate(entityFile)) {
                    writer(objectMap, templateFilePath("/templates/entityKey.java"), entityFile);
                }
            }
        }
    }

    /**
     * <p>
     * 渲染对象 MAP 信息
     * </p>
     *
     * @param tableInfo 表信息对象
     * @return
     */
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap = super.getObjectMap(tableInfo);
        //
//        objectMap.put("moduleName", this.frontEndConfigBuilder.getModuleName());
        //
        CustomTableInfo customTableInfo = (CustomTableInfo) tableInfo;
        Set<String> set = customTableInfo.getKeyFields();
        List<TableField> keyFieldList = new ArrayList<>(set.size());
        for(String fieldName: set) {
            TableField field = findTableField(tableInfo, fieldName);
            keyFieldList.add(field);
            //
            if(!"source".equals(fieldName)) {
                objectMap.put("idKeyFieldName", fieldName); //主键字段名
                objectMap.put("idKeyField", field);
            }
        }
        objectMap.put("keyFieldList", keyFieldList);
        return objectMap;
    }

    private static TableField findTableField(TableInfo tableInfo, String fieldName) {
        for(TableField field : tableInfo.getFields()) {
            if(fieldName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * <p>
     * 渲染对象 MAP 信息(前端)
     * </p>
     *
     * @param tableInfo 表信息对象
     * @return
     */
    public Map<String, Object> getObjectMap(TableInfo tableInfo, Map<String, Object> baseObjectMap, Map.Entry<String, List<Field>> entry) {
        List<Field> fieldList = entry.getValue();
        String template = entry.getKey();

        Map<String, Object> objectMap = new HashMap<>(baseObjectMap.size() + 2);
        objectMap.putAll(baseObjectMap);
        objectMap.put("fields", fieldList);
        objectMap.put("pageEnName", fieldList.get(0).getPageEnname());
        //
        String pageFile = frontEndConfigBuilder.getPageFile(template); //  "/pages/field/fieldAdd"
        objectMap.put("pagePath", pageFile);
        objectMap.put("routerPath", StringUtils.countMatches(pageFile, '/') > 1 ? pageFile.substring(pageFile.indexOf('/', 1) + 1) : pageFile); //field/fieldAdd

        objectMap.put("pageComponent", StringUtils.isEmpty(pageFile) ? null : pageFile.substring(pageFile.lastIndexOf('/') + 1)); //fieldAdd
        return objectMap;
    }


    /**
     * 是否产生后端代码
     * @return
     */
    private boolean isGenBackEnd() {
        String dir = getConfigBuilder().getGlobalConfig().getOutputDir();
        return dir != null && !"".equals(dir);
    }

    /**
     * 是否产生前端代码
     * @return
     */
    private boolean isGenFrontEndCode(String pathKey) {
        if(frontEndConfigBuilder == null) {
            return false;
        }
        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
        String dir = pathInfo.get(pathKey);
        return dir != null && !"".equals(dir);
    }

    /**
     * 是否产生前端代码
     * @return
     */
    private boolean isGenFrontEnd() {
        return isGenFrontEndCode(GenConsts.FRONT_END_PATH);
    }

    /**
     * 是否产生前端MENU代码
     * @return
     */
    private boolean isGenFrontEndMenu() {
        //有产生了前端代码文件
        if(frontEndCreatedList.isEmpty()) {
            return false;
        }
        return isGenFrontEndCode(GenConsts.MENU_PATH);
    }

    /**
     * 是否产生前端Router代码
     * @return
     */
    private boolean isGenFrontEndRouter() {
        //有产生了前端代码文件
        if(frontEndCreatedList.isEmpty()) {
            return false;
        }
        return isGenFrontEndCode(GenConsts.ROUTER_PATH);
    }


}

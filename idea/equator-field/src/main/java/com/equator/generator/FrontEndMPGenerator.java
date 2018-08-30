package com.equator.generator;


import com.equator.EquatorFieldApplication;
import com.equator.context.SpringContext;
import com.equator.model.Field;
import com.equator.service.field.FieldUtils;
import com.equator.service.field.PageFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import java.util.*;

/**
 * 如果修改了代码生成器 或 模板，注意修改修改号
 * @version ersion
 */
public class FrontEndMPGenerator {

    private static final Logger logger = LoggerFactory.getLogger(FrontEndMPGenerator.class);

    public static void main(String[] args) {
        SpringApplication.run(EquatorFieldApplication.class, args);

        new FrontEndMPGenerator("costInfo")
                .setAuthor("lwd")
                //.setOutputDir("G:\\vue_project\\tmpsrc")
                .setOutputDir("C:\\Users\\lenovo\\Desktop\\Vue\\template")

                .execute();
    }

    private Map<String, List<Field>> fieldsMap;

    private  String[] pageNames;

    private String outputDir;

    private String author;

    private final static String CONFIG_VERSION = "1.0";

    /**
     * 模板引擎
     */
    private FrontEndFreemarkerTemplateEngine templateEngine;

    public FrontEndMPGenerator(String... pageNames) {

        //TODO 不继承？ SpringApplication.run(EquatorFieldApplication.class, args); ?

        this.pageNames = pageNames;

        //先取表信息,再设置父类的表名之类？


    }

    public FrontEndMPGenerator setOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public FrontEndMPGenerator setAuthor(String author) {
        this.author = author;
        return this;
    }

    protected void init() {
        this.fieldsMap = new HashMap<>(pageNames.length);
        PageFieldService pageFieldService = SpringContext.getBean(PageFieldService.class);
        for(String pageName : this.pageNames) {
            List<Field> fieldList = pageFieldService.listFieldsByPageEnname(pageName);
            if(fieldList.isEmpty()) {
                continue;
            }
            fieldsMap.put(pageName, fieldList);
        }


    }


    /**
     * 生成代码
     */
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置
        init();
        //
        if (null == templateEngine) {
            templateEngine = new FrontEndFreemarkerTemplateEngine();
        }
        // 模板引擎初始化执行文件输出
        //param: path, obj
        Map<String, Object> data = new HashMap<>();
        data.put("author", author);
        data.put("author", author);
        data.put("author", author);
        templateEngine.init(outputDir).batchOutput(fieldsMap, data).open();
        logger.debug("==========================文件生成完成！！！==========================");
    }



    /**
     * 配置版本
     * @return
     */
    protected String version() {
        return CONFIG_VERSION;
    }






}
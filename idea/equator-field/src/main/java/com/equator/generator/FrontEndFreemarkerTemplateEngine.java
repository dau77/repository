package com.equator.generator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.equator.model.Field;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontEndFreemarkerTemplateEngine {

    protected static final Logger logger = LoggerFactory.getLogger(FrontEndFreemarkerTemplateEngine.class);

    private Configuration configuration;

    private String ouputPath;

    public FrontEndFreemarkerTemplateEngine init(String outputPath) {
        this.ouputPath = outputPath;

        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, StringPool.SLASH);
        return this;
    }


    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
        fileOutputStream.close();
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    public String templateFilePath(String filePath) {
        StringBuilder fp = new StringBuilder();
        fp.append(filePath).append(".ftl");
        return fp.toString();
    }



    /**
     * <p>
     * 渲染对象 MAP 信息
     * </p>
     *
     * @param fieldList 页面字段信息对象
     * @param data 其它
     * @return
     */
    public Map<String, Object> getObjectMap(List<Field> fieldList, Map<String, Object> data) {
        int initSize = data != null ? data.size() + 2 : 2;
        Map<String, Object> objectMap = new HashMap<>(initSize);
        objectMap.putAll(data);
        objectMap.put("fields", fieldList);
        objectMap.put("pageEnName", fieldList.get(0).getPageEnname());
        objectMap.put("tableName", fieldList.get(0).getTableName());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return objectMap;
    }











    /**
     * <p>
     * 输出 java xml 文件
     * </p>
     */
    public FrontEndFreemarkerTemplateEngine batchOutput(Map<String, List<Field>> fieldsMap, Map<String, Object> data) {
        try {
            for(Map.Entry<String, List<Field>> entry : fieldsMap.entrySet()) {
                Map<String, Object> objectMap = getObjectMap(entry.getValue(), data);

                String file = this.ouputPath + File.separator + entry.getKey() + ".vue";
                if(isCreate(file)) {
                    writer(objectMap, "/templates/component.vue.ftl", file);
                    //writer(objectMap, "/templates/fieldlist.vue.ftl", file);
                }
            }

        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

//    /**
//     * <p>
//     * 处理输出目录
//     * </p>
//     */
//    public AbstractTemplateEngine mkdirs() {
//        getConfigBuilder().getPathInfo().forEach((key, value) -> {
//            File dir = new File(value);
//            if (!dir.exists()) {
//                boolean result = dir.mkdirs();
//                if (result) {
//                    logger.debug("创建目录： [" + value + "]");
//                }
//            }
//        });
//        return this;
//    }
//
//
//    /**
//     * <p>
//     * 打开输出目录
//     * </p>
//     */
//    public void open() {
//        String outDir = getConfigBuilder().getGlobalConfig().getOutputDir();
//        if (getConfigBuilder().getGlobalConfig().isOpen()
//                && StringUtils.isNotEmpty(outDir)) {
//            try {
//                String osName = System.getProperty("os.name");
//                if (osName != null) {
//                    if (osName.contains("Mac")) {
//                        Runtime.getRuntime().exec("open " + outDir);
//                    } else if (osName.contains("Windows")) {
//                        Runtime.getRuntime().exec("cmd /c start " + outDir);
//                    } else {
//                        logger.debug("文件输出目录:" + outDir);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



    /**
     * 检测文件是否存在
     *
     * @return 是否
     */
    protected boolean isCreate(String filePath) {
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            mkDir(file.getParentFile());
        }
//        return !exist || getConfigBuilder().getGlobalConfig().isFileOverride();
        return !exist; //存在就不产生
    }

    protected void mkDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }


    /**
     * <p>
     * 打开输出目录
     * </p>
     */
    public void open() {
        try {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + this.ouputPath);
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start " + this.ouputPath);
                } else {
                    logger.debug("文件输出目录:" + this.ouputPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

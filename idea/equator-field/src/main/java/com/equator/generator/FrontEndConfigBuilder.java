package com.equator.generator;

import com.equator.context.SpringContext;
import com.equator.model.Field;
import com.equator.service.field.FieldUtils;
import com.equator.service.field.PageFieldService;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FrontEndConfigBuilder {

    //表名 -> 模板名 -> 字段信息
    private Map<String, Map<String, List<Field>>> tableFieldsMap = new HashMap<>();

    private Map<String, List<String>> pageMap;

//    private String outputDir;

//    private String moduleName;

    /**
     *
     * @param pageMap 模板名 -> [页面名,文件名]
     */
    public FrontEndConfigBuilder(Map<String, List<String>> pageMap) {
        this.pageMap = pageMap;
        init();
    }

    protected void init() {
        PageFieldService pageFieldService = SpringContext.getBean(PageFieldService.class);
        for(Map.Entry<String, List<String>> entry : pageMap.entrySet()) {
            if(StringUtils.isEmpty(entry.getKey()) || entry.getValue() == null || entry.getValue().size() < 2
                    || StringUtils.isEmpty(entry.getValue().get(0)) || StringUtils.isEmpty(entry.getValue().get(1))) {
                continue; //都非空才处理
            }
            String pageName = entry.getValue().get(0);
            List<Field> fieldList = pageFieldService.listFieldsByPageEnname(pageName);
            if(fieldList == null || fieldList.isEmpty()) {
                continue; //如果没有，则不处理
            }
            for(Field field : fieldList) {
                FieldUtils.convertFieldMessage(field);
            }
            //
            String tableName = fieldList.get(0).getTableName();
            if(!tableFieldsMap.containsKey(tableName)) {
                tableFieldsMap.put(tableName, new HashMap<>());
            }
            tableFieldsMap.get(tableName).put(entry.getKey(), fieldList); //表名 -> 模板名 -> List<Field>
        }
    }

    /**
     * 返回指定表相关页面的Field
     * @param tableName
     * @return
     */
    public Map<String, List<Field>> getFieldsMap(String tableName) {
        return tableFieldsMap.get(tableName);
    }

//    /**
//     * 获取模板名
//     * @param pageName
//     * @return
//     */
//    public String getTemplateName(String pageName) {
//        return this.pageNameMap.get(pageName);x
//    }

    /**
     * 获取页面
     * @param templateName
     * @return
     */
    public String getPageName(String templateName) {
        return this.pageMap.get(templateName).get(0);
    }

    /**
     * 获取页面文件名
     * @param templateName
     * @return
     */
    public String getPageFile(String templateName) {
        return this.pageMap.get(templateName).get(1);
    }
}

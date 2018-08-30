package com.equator.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeneratorParam {

    private String author;

    private String frontEndOutputDir;

    private String backEndOutputDir;

    private String menuFile;

    private String routerFile;

    private Boolean fileOverride;

    private String parentPackageName;

    private String tableName;

    private String moduleName;

//    private Map<String, String> pageNameMap;

    private Map<String, List<String>> pageMap;

    //TODO 后端模块名？
}

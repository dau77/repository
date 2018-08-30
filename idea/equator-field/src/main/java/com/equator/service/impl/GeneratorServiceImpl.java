package com.equator.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.equator.generator.FrontEndConfigBuilder;
import com.equator.generator.MPGenerator;
import com.equator.model.GeneratorParam;
import com.equator.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Override
    public void all(GeneratorParam param) {




        MPGenerator generator = new MPGenerator(param.getParentPackageName(), param.getTableName())
                .setFileOverride(param.getFileOverride())
                .setAuthor(param.getAuthor())
                .setOutputDir(param.getBackEndOutputDir())
                .setDataSourceConfig(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                .setParam(param)
//                .putInjection("moduleName", param.getModuleName())
                ;

        if(param.getPageMap() != null && !param.getPageMap().isEmpty() && StringUtils.isNotEmpty(param.getFrontEndOutputDir())) {
//            FrontEndConfigBuilder frontEndConfigBuilder = new FrontEndConfigBuilder(param.getPageNames().split(","));
//            frontEndConfigBuilder.setOutputDir(param.getFrontEndOutputDir());
//            generator.setFrontEndConfigBuilder(frontEndConfigBuilder);

            FrontEndConfigBuilder frontEndConfigBuilder = new FrontEndConfigBuilder(param.getPageMap());
//            frontEndConfigBuilder.setOutputDir(param.getFrontEndOutputDir());
//            frontEndConfigBuilder.setModuleName(param.getModuleName());
            generator.setFrontEndConfigBuilder(frontEndConfigBuilder);
        }


        generator.execute();
    }
}

package com.equator.json.converter;

import com.equator.model.SystemConfig;
import com.equator.service.systemconfig.SystemConfigService;
import com.equator.context.SpringContext;
import org.springframework.core.convert.converter.Converter;

/**
 * 用于将SystemConfig的value转为name
 */
//TODO 待优化
public class SystemConfigConverter implements Converter<SystemConfig, String> {

//    @Autowired
//    private SystemConfigService systemConfigService;

    @Override
    public String convert(SystemConfig source) {
        SystemConfigService systemConfigService = SpringContext.getBean(SystemConfigService.class);
        return systemConfigService.selectNameByValue(source.getCategory(), source.getKeyValue());
    }
}

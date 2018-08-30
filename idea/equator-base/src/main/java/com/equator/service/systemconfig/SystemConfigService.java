package com.equator.service.systemconfig;

import com.equator.model.SystemConfig;
import com.equator.service.BService;

import java.util.List;

public interface SystemConfigService extends BService<SystemConfig> {

    /**
     * 根据类别获取系统配置列表
     * @param category
     * @return
     */
    List<SystemConfig> selectListByCategory(String category);

    /**
     * 通过类别和值获取名称
     * @param category
     * @param value
     * @return
     */
    String selectNameByValue(String category, String value);
}

package com.equator.service.systemconfig;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.equator.dao.SystemConfigMapper;
import com.equator.model.SystemConfig;
import com.equator.service.BServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigServiceImpl extends BServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Override
    @Cacheable(value = "system_config_by_category", key = "#category")
    public List<SystemConfig> selectListByCategory(String category) {
        Wrapper<SystemConfig> wrapper = new QueryWrapper<SystemConfig>().eq("category", category);
        return list(wrapper);
    }

    @Override
    @Cacheable(value = "system_config_value_to_name", key = "#category + '_' + #value")
    public String selectNameByValue(String category, String value) {
        Wrapper<SystemConfig> wrapper = new QueryWrapper<SystemConfig>().eq("category", category)
                .eq("key_value", value);
        SystemConfig systemConfig = getOne(wrapper);
        return systemConfig == null ? "" : systemConfig.getKeyName();
    }
}

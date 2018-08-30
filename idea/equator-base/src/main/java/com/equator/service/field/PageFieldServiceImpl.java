package com.equator.service.field;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.equator.dao.FieldMapper;
import com.equator.model.Field;
import com.equator.model.FieldType;
import com.equator.service.BServiceImpl;
import com.equator.service.systemconfig.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageFieldServiceImpl extends BServiceImpl<FieldMapper, Field> implements PageFieldService {

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public Map<String, Field> getMapByPageEnname(String pageEnname) {
        Wrapper<Field> wrapper = new QueryWrapper<Field>().eq("page_enname", pageEnname);
        List<Field> fieldList = list(wrapper);
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field: fieldList){
            appendSystemConfig(field);
            FieldUtils.convertFieldMessage(field);
            fieldMap.put(field.getFieldEnname(), field);
        }
        return fieldMap;
    }

    @Override
    public List<Field> listFieldsByPageEnname(String pageEnname) {
        Wrapper<Field> wrapper = new QueryWrapper<Field>()
                .eq("page_enname", pageEnname)
                .orderByAsc("field_order_by");
        return list(wrapper);
    }

    @Override
    public List<Field> selectListByField(Field field) {
        Wrapper<Field> wrapper = new QueryWrapper<Field>().eq("page_enname", field.getPageEnname())
                .eq("table_name", field.getTableName());
        return list(wrapper);
    }

    /**
     * 追加系统配置信息
     * @param field
     */
    public void appendSystemConfig(Field field) {
        FieldType fieldType = FieldType.getByCode(field.getFieldType());
        if (fieldType == null) {
            // ??
            return;
        }
        switch (fieldType) {
            case SELECT:
            case RADIO:
            case CHECKBOX:
                field.setCategoryGroup(systemConfigService.selectListByCategory(field.getFieldTypeParam()));
                break;
            default:
                break;
        }
    }
}

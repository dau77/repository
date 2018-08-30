package com.equator.service.field;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.equator.model.Field;
import com.equator.service.BService;

import java.io.Serializable;
import java.util.Locale;


public interface FieldService extends BService<Field> {

    /**
     * 获取分页列表
     * @param param
     * @return
     */
    IPage<Field> page(FieldListSearchParam param);

    /**
     * 根据ID获取信息
     * @param fieldId
     * @param localeStr
     * @return
     */
    Field getById(Serializable fieldId, String localeStr);

}

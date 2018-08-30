package com.equator.service.field;

import com.equator.model.Field;
import com.equator.service.BService;

import java.util.List;
import java.util.Map;

public interface PageFieldService extends BService<Field> {

    /**
     * 获取页面字段信息
     * @param pageEnname
     * @return
     */
    Map<String, Field> getMapByPageEnname(String pageEnname);

    /**
     * 获取页面字段信息
     * @param pageEnname
     * @return
     */
    List<Field> listFieldsByPageEnname(String pageEnname);

    /**
     *
     * @return
     */
    List<Field> selectListByField(Field field);
}

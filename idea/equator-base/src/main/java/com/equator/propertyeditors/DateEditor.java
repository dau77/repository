package com.equator.propertyeditors;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Date date = null;
        if(StringUtils.isNotEmpty(text)) { //为空则返回null
            date = new Date(Long.valueOf(text));
        }
        setValue(date);
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}

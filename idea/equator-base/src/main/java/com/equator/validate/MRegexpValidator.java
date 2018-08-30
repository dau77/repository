package com.equator.validate;

import com.equator.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MRegexpValidator implements Validator<String> {
    private String regexp;

    public MRegexpValidator(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean validate(String value) {
        if (StringUtils.isEmpty(regexp)) {
            return true;
        }
        Pattern pattern = Pattern.compile(regexp);
        Matcher m = pattern.matcher(value);
        if (!m.matches()) {
            return false;
        }
        return true;
    }
}

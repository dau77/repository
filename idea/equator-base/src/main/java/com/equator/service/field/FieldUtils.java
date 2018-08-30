package com.equator.service.field;

import com.equator.exception.ErrorException;
import com.equator.model.Field;
import com.equator.context.MessageManager;
import com.equator.util.JsonHandler;
import com.equator.util.StringUtils;
import com.equator.validate.FieldRules;
import com.equator.validate.Rule;

import java.io.IOException;
import java.util.Locale;

/**
 * 字段工具
 */
public class FieldUtils {

    public static void convertFieldMessage(Field field) {
        convertFieldMessage(field, Locale.CHINA);
    }

    /**
     * 转换字段数据中消息内容
     * @param field     字段信息
     * @param locale    语言
     */
    public static void convertFieldMessage(Field field, Locale locale) {
        if (field == null) {
            return;
        }
        field.setFieldName(getConvertMessage(field.getFieldName(), locale));
        field.setFieldPlaceholder(getConvertMessage(field.getFieldPlaceholder(), locale));
        field.setPageName(getConvertMessage(field.getPageName(), locale));
        try {
            convertFieldRulesMesssage(field, locale);
        } catch (IOException e) {
            throw new ErrorException("验证规则格式不正确！");
        }
    }

    private static String getConvertMessage(String key, Locale locale) {
        String msg = MessageManager.getMsg(key, locale);
        return msg.equals(key) ? "" : msg;
    }

    private static void convertFieldRulesMesssage(Field field, Locale locale) throws IOException {
        if (StringUtils.isEmpty(field.getFieldRules())) {
            return;
        }
        FieldRules fieldRules = JsonHandler.json2Object(field.getFieldRules(), FieldRules.class);
        for (Rule rule : fieldRules.getRuleList()) {
            rule.setMessage(getConvertMessage(rule.getMessage(), locale));
        }
        field.setFieldRules(JsonHandler.object2Json(fieldRules));
    }

}

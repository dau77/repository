package com.equator.validate;

import com.equator.model.Field;
import com.equator.util.JsonHandler;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class MRulesValidator {
    private FieldRules fieldRules;

    private ConstraintValidatorContext context;

    MRulesValidator(Field field, ConstraintValidatorContext context) throws IOException {
        this.fieldRules = JsonHandler.json2Object(field.getFieldRules(), FieldRules.class);
        this.context = context;
    }

    public boolean validate(Object value) {
        if (CollectionUtils.isEmpty(fieldRules.getRuleList())) {
            return true;
        }
        //
        Validator validator;
        for (Rule rule : fieldRules.getRuleList()) {
            validator = buildValidator(rule, value);
            if (!validator.validate(value)) {
                setMessage(rule);
                return false;
            }
        }
        return true;
    }

    private Validator buildValidator(Rule rule, Object value) {
        if (value == null || value instanceof String) {
            return new MStringValidator(rule);
        }
        else if (value instanceof Short) {
            return new MShortValidator(rule);
        }
        else if (value instanceof Integer) {
            return new MIntegerValidator(rule);
        }
        else if (value instanceof Double) {
            return new MDoubleValidator(rule);
        }

        throw new RuntimeException("创建验证器发生异常，类型未知！");
    }

    /**
     * 设置消息
     * @param rule
     */
    private void setMessage(Rule rule) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(rule.getMessage()).addConstraintViolation();
    }

}

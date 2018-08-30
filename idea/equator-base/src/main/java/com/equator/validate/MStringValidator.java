package com.equator.validate;


import com.equator.util.StringUtils;

public class MStringValidator implements Validator<String> {
    private Rule rule;

    MStringValidator(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean validate(String value) {
        // 非必填但为空时，后续就不需要处理了
        if (!rule.getRequired() && StringUtils.isEmpty(value)) {
            return true;
        }
        // 必填
        if (rule.getRequired() && StringUtils.isEmpty(value)) {
            return false;
        }

        // 长度
        if (rule.getLen() != null && value.length() != rule.getLen())  {
            return false;
        }

        // 范围
        Validator validator = new MRangeValidator(rule.getMin(), rule.getMax());
        if (!validator.validate(value.length())) {
            return false;
        }

        // 正则
        validator = new MRegexpValidator(rule.getPattern());
        if (!validator.validate(value)) {
            return false;
        }

        // 其他验证
        validator = buildValidator();
        if (validator != null && !validator.validate(value)) {
            return false;
        }

        return true;
    }

    private Validator buildValidator() {
        ValidateType validateType = ValidateType.getByValue(rule.getType());
        switch (validateType) {
            case STRING:
                return null;
            case EMAIL:
                return new MEmailValidator();
            case REGEXP:
                return new MRegexpValidator(rule.getPattern());
        }

        throw new RuntimeException("创建验证器发生异常，类型未知！");
    }
}

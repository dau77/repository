package com.equator.validate;

public class MShortValidator implements Validator<Short> {
    private Rule rule;

    MShortValidator(Rule rule) {
        this.rule = rule;

    }

    @Override
    public boolean validate(Short value) {
        Validator validator = new MRangeValidator(rule.getMin(), rule.getMax());
        if (!validator.validate(value.intValue())) {
            return false;
        }
        return true;
    }
}

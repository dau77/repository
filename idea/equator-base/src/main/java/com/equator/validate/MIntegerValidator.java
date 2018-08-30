package com.equator.validate;

public class MIntegerValidator implements Validator<Integer> {
    private Rule rule;

    MIntegerValidator(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean validate(Integer value) {
        Validator validator = new MRangeValidator(rule.getMin(), rule.getMax());
        if (!validator.validate(value)) {
            return false;
        }
        return true;
    }
}

package com.equator.validate;

public class MDoubleValidator implements Validator<Double> {
    private Rule rule;

    public MDoubleValidator(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean validate(Double value) {

        return true;
    }
}

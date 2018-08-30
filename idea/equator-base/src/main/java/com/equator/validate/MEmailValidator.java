package com.equator.validate;


public class MEmailValidator implements Validator<String> {
    private final static String EMAIL_PATTERN = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public MEmailValidator() {

    }

    @Override
    public boolean validate(String value) {
        Validator validator = new MRegexpValidator(EMAIL_PATTERN);
        if (!validator.validate(value)) {
            return false;
        }
        return true;
    }
}

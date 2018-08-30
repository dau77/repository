package com.equator.validate;

public enum ValidateType {
    STRING("string"), METHOD("method"), EMAIL("email"), REGEXP("regexp");

    private String value;

    ValidateType(String value) {
        this.value = value;
    }

    public static ValidateType getByValue(String value) {
        for (ValidateType validateType : ValidateType.values()) {
            if (value.equals(validateType.getValue())) {
                return validateType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}

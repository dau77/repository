package com.equator.model;

public enum FieldType {
    INPUT(1), PASSWORD(2), SELECT(3), TEXTAREA(4), RADIO(5), CHECKBOX(6), HIDDEN(7), BUTTON(8), SUBMIT(9), DATE(10);
    int code;
    FieldType(int code) {
        this.code = code;
    }

    public static FieldType getByCode(int code) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getCode() == code) {
                return fieldType;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}

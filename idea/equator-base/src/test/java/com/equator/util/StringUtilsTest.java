package com.equator.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void camelToUnderline() {
        String str = "FieldRule";
        String expected = "field_rule";
        String actual = StringUtils.camelToUnderline(str);
        assertEquals("camelToUnderline is wrong", expected, actual);
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void uncapitalize() {
        String str = "FieldRule";
        String expected = "fieldRule";
        String actual = StringUtils.uncapitalize(str);
        assertEquals("uncapitalize is wrong", expected, actual);
    }
}
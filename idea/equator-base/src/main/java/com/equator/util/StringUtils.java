package com.equator.util;

/**
 * 字符串工具
 */
public class StringUtils {
    public static final char UNDERLINE = '_';

    /**
     * 驼峰转下划线
     *
     * @param param 需要转化的字符串
     * @return 
     */
    public static String camelToUnderline(String param) {
        if (isEmpty(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    sb.append(UNDERLINE);
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否为空
     * @param cs
     * @return
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String uncapitalize(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        String c = str.substring(0, 1);
        return c.toLowerCase() + str.substring(1, str.length());
    }
}

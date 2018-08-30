package com.equator.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 用于通过消息code获取消息内容的工具
 * TODO 待优化
 */
@Component
public class MessageManager {

    private static MessageSource messageSource;

    public static String getMsg(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMsg(key, locale);
    }

    public static String getMsg(String key, String... arg) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMsg(key, locale, arg);
    }

    public static String getMsg(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public static String getMsg(String key, Locale locale, String... arg) {
        Object[] args = new Object[arg.length];
        for (int i = 0; i < arg.length; i++) {
            args[i] = arg[i];
        }
        return messageSource.getMessage(key, args, locale);
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageManager.messageSource = messageSource;
    }
}

package com.equator.context.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.equator.dao.MessageMapper;
import com.equator.model.Message;
import com.equator.service.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库消息源
 * TODO 待优化
 */
public class DBMessageResource extends AbstractMessageSource implements ResourceLoaderAware {

//    @Autowired
//    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;

//    private final Logger log = LoggerFactory.getLogger(getClass());

    //cache resource
    private static final Map<String, Map<String, String>> LOCAL_CACHE = new ConcurrentHashMap<>(256);
    //default base name
//    private static final String DEFAULT_RESOURCE_BUND_BASE_NAME = "messages";

    private ResourceLoader resourceLoader;

    public DBMessageResource() {
    }

    @PostConstruct
    public void init() {
        this.reload();
    }

    public void reload() {
        LOCAL_CACHE.clear();//clear cache while reload data.
        LOCAL_CACHE.putAll(getAllMessageResource());
    }

    private Map<String, Map<String, String>> getAllMessageResource() {
//        List<Message> list = messageMapper.selectList(new QueryWrapper<>());
        List<Message> list = messageService.list(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        for (Message message : list) {
            String locale = message.getLocale();
            Map<String, String> resource = LOCAL_CACHE.get(locale);
            if(resource == null) {
                resource = new HashMap<>();
                LOCAL_CACHE.put(locale, resource);
            }
            resource.put(message.getMsgId(), message.getMessage());
        }
        return LOCAL_CACHE;
    }

    private String checkFromCachedOrBundResource(String code, Locale locale) {
//        String language = locale.getLanguage();
//        Map<String, String> props = LOCAL_CACHE.get(language);
        String localeCode = locale.toString();
        Map<String, String> resource = LOCAL_CACHE.get(localeCode);
        if (null != resource && resource.containsKey(code)) {
            return resource.get(code);
        } else {
            //check from parent message resource. and catch no such element exception.
            try {
                if (null != this.getParentMessageSource()) {
                    return this.getParentMessageSource().getMessage(code, null, locale);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            return code;
        }
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        //first check from local cache, is none exists. then query from parent message resource which load from bundle resrouces
        String msg = checkFromCachedOrBundResource(code, locale);
        MessageFormat messageFormat = new MessageFormat(msg, locale);
        return messageFormat;
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return checkFromCachedOrBundResource(code, locale);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
    }
}

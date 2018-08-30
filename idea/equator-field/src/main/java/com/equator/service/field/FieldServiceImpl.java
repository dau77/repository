package com.equator.service.field;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.equator.context.support.DBMessageResource;
import com.equator.dao.FieldMapper;
import com.equator.exception.ErrorException;
import com.equator.model.Field;
import com.equator.model.FieldType;
import com.equator.model.Message;
import com.equator.service.BServiceImpl;
import com.equator.service.message.MessageService;
import com.equator.service.systemconfig.SystemConfigService;
import com.equator.util.AssertUtils;
import com.equator.util.JsonHandler;
import com.equator.util.StringUtils;
import com.equator.validate.FieldRules;
import com.equator.validate.Rule;
import com.equator.validate.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class FieldServiceImpl extends BServiceImpl<FieldMapper, Field> implements FieldService {
    @Autowired
    private MessageService messageService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private MessageSource messageSource;

    @Override
    public IPage<Field> page(FieldListSearchParam param) {
        Page<Field> page = new Page<>(param.getCurrent(), param.getSize());
        page.setAscs("page_enname");
        page.setDescs("field_id");
        Locale locale = getLocale(param.getLocale());
        Wrapper<Field> wrapper = new QueryWrapper<Field>()
                .eq(!StringUtils.isEmpty(param.getPageEnname()),"page_enname", param.getPageEnname());
        IPage<Field> fieldIPage = super.page(page, wrapper);
        for (Field field : fieldIPage.getRecords()) {
            appendFieldTypeName(field);
            FieldUtils.convertFieldMessage(field, locale);
        }
        return fieldIPage;
    }

    private Locale getLocale(String locale) {
        if (StringUtils.isEmpty(locale)) {
            return Locale.CHINA;
        }
        else {
            String[] locales = locale.split("_");
            AssertUtils.isTrue(locales.length == 2, "语言格式不正确！");
            return new Locale(locales[0], locales[1]);
        }
    }

    private void appendFieldTypeName(Field field) {
        field.setFieldTypeName(systemConfigService.selectNameByValue("表单字段类型", field.getFieldType().toString()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Field field) {
        validateInsert(field);
        saveOrUpdateMessage(field);
        String username = (String) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        field.setWhoAdd(username);
        field.setAddTime(new Date());
        return super.save(field);
    }

    private void saveOrUpdateMessage(Field field) {
        List<Message> messageList = createMessageList(field);
        boolean rv = messageService.saveOrUpdateBatch(messageList);
        AssertUtils.isTrue(rv, "保存或更新消息失败！");
        ((DBMessageResource) messageSource).reload();
    }

    private List<Message> createMessageList(Field field) {
        List<Message> messageList = new ArrayList<>();
        addFieldNameMessage(messageList, field);
        addFieldPlaceholderMessage(messageList, field);
        addPageNameMessage(messageList, field);
        try {
            addFieldRulesMessage(messageList, field);
        } catch (IOException e) {
            throw new ErrorException("验证规则格式不正确！");
        }
        return messageList;
    }

    private void addFieldNameMessage(List<Message> messageList, Field field) {
        String msgId = createMsgId(field, "field_name");
        messageList.add(createMessage(msgId, field.getFieldName(), field.getLocale()));
        field.setFieldName(msgId);
    }

    private void addFieldPlaceholderMessage(List<Message> messageList, Field field) {
        String msgId = createMsgId(field, "field_placeholder");
        messageList.add(createMessage(msgId, field.getFieldPlaceholder(), field.getLocale()));
        field.setFieldPlaceholder(msgId);
    }

    private void addPageNameMessage(List<Message> messageList, Field field) {
        String msgId = createMsgId(field, "page_name");
        messageList.add(createMessage(msgId, field.getPageName(), field.getLocale()));
        field.setPageName(msgId);
    }

    private void addFieldRulesMessage(List<Message> messageList, Field field) throws IOException {
        if (StringUtils.isEmpty(field.getFieldRules())) {
            return;
        }
        FieldRules fieldRules = JsonHandler.json2Object(field.getFieldRules(), FieldRules.class);
        int index = 0;
        String msgId;
        for (Rule rule : fieldRules.getRuleList()) {
            index++;
            msgId = createMsgId(field, "field_rules." + index);
            messageList.add(createMessage(msgId, rule.getMessage(), field.getLocale()));
            rule.setMessage(msgId);
        }
        field.setFieldRules(JsonHandler.object2Json(fieldRules));
    }

    private String createMsgId(Field field, String key) {
        return field.getPageEnname() + "." + field.getFieldEnname() + "." + key;
    }

    private Message createMessage(String msgId, String messageContent, String locale) {
        Message message = new Message();
        message.setMsgId(msgId);
        message.setMessage(messageContent);
        message.setLocale(locale);
        return message;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Field field) {
        validateUpdate(field);
        saveOrUpdateMessage(field);

        String username = (String) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        field.setWhoModified(username);
        field.setModifiedTime(new Date());
        return super.updateById(field);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable fieldId) {
        validateFieldId(fieldId);
        Field field = super.getById(fieldId);
        AssertUtils.notNull(field, "删除表单字段数据不存在！");

        List<Serializable> msgIdList = createRemoveMsgIdList(field);
        boolean rv = messageService.removeByIds(msgIdList);
        AssertUtils.isTrue(rv, "删除消息失败！");
        return super.removeById(fieldId);
    }

    private List<Serializable> createRemoveMsgIdList(Field field) {
        List<Serializable> msgIdList = new ArrayList<>();
        msgIdList.add(field.getFieldName());
        msgIdList.add(field.getFieldPlaceholder());
        msgIdList.add(field.getPageName());
        if (!StringUtils.isEmpty(field.getFieldRules())) {
            FieldRules fieldRules;
            try {
                fieldRules = JsonHandler.json2Object(field.getFieldRules(), FieldRules.class);
            } catch (IOException e) {
                throw new ErrorException("验证规则格式不正确！");
            }
            for (Rule rule : fieldRules.getRuleList()) {
                msgIdList.add(rule.getMessage());
            }
        }
        return msgIdList;
    }

    public Field getById(Serializable fieldId, String localeStr) {
        validateFieldId(fieldId);
        Locale locale = getLocale(localeStr);
        Field field = super.getById(fieldId);
        FieldUtils.convertFieldMessage(field, locale);
        return field;
    }

    /**
     * 验证添加信息
     * @param field
     */
    private void validateInsert(Field field) {
        validateBase(field);

        validateIsExists(field);
    }

    private void validateIsExists(Field field) {
        Wrapper<Field> wrapper = new QueryWrapper<Field>()
                .eq("page_enname", field.getPageEnname())
                .eq("field_enname", field.getFieldEnname());
        int count = super.count(wrapper);
        AssertUtils.isTrue(count == 0, "页面字段已存在请勿重复添加！");
    }

    /**
     * 验证更新信息
     * @param field
     */
    private void validateUpdate(Field field) {
        validateFieldId(field.getFieldId());
        Field tField = super.getById(field.getFieldId());
        AssertUtils.notNull(tField, "更新表单字段数据不存在！");
        validateBase(field);
    }

    /**
     * 验证主键
     * @param fieldId
     */
    private void validateFieldId(Serializable fieldId) {
        if (fieldId == null) {
            throw new ErrorException("主键参数不存在！");
        }
    }

    /**
     * 验证基本信息
     * @param field
     */
    private void validateBase(Field field) {
        // TODO 这里的异常处理需要自己定义一个
        // TODO 这里是否还需要手工写？
        ValidateUtils.validate(field, Field.FieldAdd.class);

//        if (StringUtils.isEmpty(field.getLocale())) {
//            field.setLocale(Locale.CHINA.toString());
//        }
//        else {
//            String[] locales = field.getLocale().split("_");
//            AssertUtils.isTrue(locales.length == 2, "语言格式不正确！");
//        }
//
//        if (StringUtils.isEmpty(field.getFieldName())) {
//            throw new ErrorException("表单字段名称不能为空！");
//        }
//        if (StringUtils.isEmpty(field.getFieldEnname())) {
//            throw new ErrorException("表单字段英文名不能为空！");
//        }

        validateFieldType(field);

//        if (StringUtils.isEmpty(field.getTableName())) {
//            throw new ErrorException("对应表名不能为空！");
//        }
//        if (StringUtils.isEmpty(field.getTableField())) {
//            throw new ErrorException("对应表字段不能为空！");
//        }
//
//        if (StringUtils.isEmpty(field.getPageName())) {
//            throw new ErrorException("页面名称不能为空！");
//        }
//        if (StringUtils.isEmpty(field.getPageEnname())) {
//            throw new ErrorException("页面英文名称不能为空！");
//        }
//        if (StringUtils.isEmpty(field.getPageEntity())) {
//            throw new ErrorException("页面实体不能为空！");
//        }
    }

    private void validateFieldType(Field field) {
        if (field.getFieldType() == null) {
            throw new ErrorException("表单字段类型不能为空！");
        }
        FieldType fieldType = FieldType.getByCode(field.getFieldType());
        if (fieldType == null) {
            throw new ErrorException("表单字段类型有误！");
        }
        switch (fieldType) {
            case SELECT:
            case RADIO:
            case CHECKBOX:
                if (StringUtils.isEmpty(field.getFieldTypeParam())) {
                    throw new ErrorException("表单字段类型参数不能为空！");
                }
                break;
            default:
                break;
        }
    }
}

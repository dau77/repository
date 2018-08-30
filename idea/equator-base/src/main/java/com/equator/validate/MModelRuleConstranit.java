package com.equator.validate;

import com.equator.model.Field;
import com.equator.service.field.PageFieldService;
import com.equator.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//每个验证的地方初始化后一直使用同一个对象？
public class MModelRuleConstranit implements ConstraintValidator<MModelRule, Object> { //<注解类,字段类型>

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PageFieldService pageFieldService;

    @Override
    public void initialize(MModelRule constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object[] groups = ValidateAspect.getGroups();
        if (ArrayUtils.isEmpty(groups)) {
            return true;
        }
        String pageEnname = getPageEnname(groups);
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String table = StringUtils.camelToUnderline(beanWrapper.getWrappedClass().getSimpleName());
        List<Field> fieldList = pageFieldService.selectListByField(getFieldObj(pageEnname, table));
        if (fieldList == null) {
            return true;
        }
        Map<String, Field> fieldMap = convertFieldListToFieldMap(fieldList);
        String fieldName;
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        MRulesValidator v;
        Method method;
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            if (!"class".equals(descriptor.getName())) {
                logger.debug(descriptor.getName());
                method = descriptor.getReadMethod();
                fieldName = StringUtils.camelToUnderline(descriptor.getName());
                Field field = fieldMap.get(fieldName);
                if (field != null && !StringUtils.isEmpty(field.getFieldRules())) {
                    try {
                        v = new MRulesValidator(field, context);
                        if (!v.validate(method.invoke(value))) {
                            return false;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("构造验证器发生异常！", e);
                    }
                }
            }
        }

        return true;
    }

    private Map<String, Field> convertFieldListToFieldMap(List<Field> fieldList) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fieldList) {
            fieldMap.put(field.getFieldEnname(), field);
        }
        return fieldMap;
    }

    private Field getFieldObj(String pagenEnname, String table) {
        Field fieldObj = new Field();
        fieldObj.setPageEnname(pagenEnname);
        fieldObj.setTableName(table);
        return fieldObj;
    }

    private String getPageEnname(Object[] groups) {
        Class clazz = (Class) groups[0];
        logger.debug(clazz.getSimpleName() + "========>" + StringUtils.uncapitalize(clazz.getSimpleName()));
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }

}
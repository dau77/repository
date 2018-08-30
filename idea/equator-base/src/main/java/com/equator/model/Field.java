package com.equator.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
* Created by Mybatis Generator (LombokPlugin) 2018-07-13 16:52:27
*/
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field extends BModel {
    public interface FieldAdd extends Default {}

    /**
    * This method returns the value of the database column field.field_id
    */
    @TableId
    private Integer fieldId;

    /**
    * This method returns the value of the database column field.field_name
    */
    private String fieldName;

    /**
    * This method returns the value of the database column field.field_enname
    */
    private String fieldEnname;

    /**
    * This method returns the value of the database column field.field_length
    */
    private Short fieldLength;

    /**
    * This method returns the value of the database column field.field_type
    */
    private Short fieldType;

    /**
    * This method returns the value of the database column field.field_type_param
    */
    private String fieldTypeParam;

    /**
    * This method returns the value of the database column field.field_order_by
    */
    private Short fieldOrderBy;

    /**
    * This method returns the value of the database column field.field_placeholder
    */
    private String fieldPlaceholder;

    /**
    * This method returns the value of the database column field.field_rules
    */
    private String fieldRules;

    /**
    * This method returns the value of the database column field.table_name
    */
    private String tableName;

    /**
    * This method returns the value of the database column field.table_field
    */
    private String tableField;

    /**
    * This method returns the value of the database column field.page_name
    */
    private String pageName;

    /**
     * This method returns the value of the database column field.page_enname
     */
    private String pageEnname;

    /**
    * This method returns the value of the database column field.page_entity
    */
    private String pageEntity;

    /**
     * This method returns the value of the database column field.add_time
     */
    private Date addTime;

    /**
     * This method returns the value of the database column field.who_add
     */
    private String whoAdd;

    /**
     * This method returns the value of the database column field.modified_time
     */
    private Date modifiedTime;

    /**
     * This method returns the value of the database column field.who_modified
     */
    private String whoModified;

    @TableField(exist = false)
    private List<SystemConfig> categoryGroup;

    @TableField(exist = false)
    private String fieldTypeName;

    @TableField(exist = false)
    private String locale;
}
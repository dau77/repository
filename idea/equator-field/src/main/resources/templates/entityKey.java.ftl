package ${package.Entity};

<#list table.keyImportPackages as pkg>
import ${pkg};
</#list>
<#if entityLombokModel>

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
</#if>

/**
 * <p>
 * ${table.comment!table.name}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 * @version ${cfg.gen_version}_1.0
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#-- ----------  暂不处理activeRecord的情况  ---------->
<#if superEntityClass??>
public class ${entity}Key extends ${superEntityClass} {
<#else>
public class ${entity}Key implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;

    public ${entity}Key() {}

    public ${entity}Key(<#list keyFieldList as field>${field.propertyType} ${field.propertyName}<#if field?has_next>, </#if></#list>) {
<#list keyFieldList as field>
        this.${field.propertyName} = ${field.propertyName};
</#list>
    }


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if table.keyFields?seq_contains(field.name)>

        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
        </#if>
        <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
        </#if>
        <#if field.keyFlag>
        <#-- 主键 -->
            <#if field.keyIdentityFlag>
    @TableId(value = "${field.name}", type = IdType.AUTO)
            <#elseif idType??>
    @TableId(value = "${field.name}", type = IdType.${idType})
            <#elseif field.convert>
    @TableId("${field.name}")
            <#else><#-- 如果没有@TableId注解，使用xxxById方法时会不认识主键字段 -->
    @TableId
            </#if>
        <#-- 普通字段 -->
        <#elseif field.fill??>
        <#-- -----   存在字段填充设置   ----->
            <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
            <#else>
    @TableField(fill = FieldFill.${field.fill})
            </#if>
        <#elseif field.convert>
    @TableField("${field.name}")
        </#if>
    <#-- 乐观锁注解 -->
        <#if (versionFieldName!"") == field.name>
    @Version
        </#if>
    <#-- 逻辑删除注解 -->
        <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
        </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->

<#--<#if !entityLombokModel>-->
    <#--<#list table.fields as field>-->
        <#--<#if field.propertyType == "boolean">-->
            <#--<#assign getprefix="is"/>-->
        <#--<#else>-->
            <#--<#assign getprefix="get"/>-->
        <#--</#if>-->
    <#--public ${field.propertyType} ${getprefix}${field.capitalName}() {-->
        <#--return ${field.propertyName};-->
    <#--}-->

        <#--<#if entityBuilderModel>-->
    <#--public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {-->
        <#--<#else>-->
    <#--public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {-->
        <#--</#if>-->
        <#--this.${field.propertyName} = ${field.propertyName};-->
        <#--<#if entityBuilderModel>-->
        <#--return this;-->
        <#--</#if>-->
    <#--}-->
    <#--</#list>-->
<#--</#if>-->

<#--<#if entityColumnConstant>-->
    <#--<#list table.fields as field>-->
    <#--public static final String ${field.name?upper_case} = "${field.name}";-->

    <#--</#list>-->
<#--</#if>-->
<#--<#if activeRecord>-->
    <#--@Override-->
    <#--protected Serializable pkVal() {-->
    <#--<#if keyPropertyName??>-->
        <#--return this.${keyPropertyName};-->
    <#--<#else>-->
        <#--return null;-->
    <#--</#if>-->
    <#--}-->

<#--</#if>-->
<#--<#if !entityLombokModel>-->
    <#--@Override-->
    <#--public String toString() {-->
        <#--return "${entity}{" +-->
    <#--<#list table.fields as field>-->
        <#--<#if field_index==0>-->
        <#--"${field.propertyName}=" + ${field.propertyName} +-->
        <#--<#else>-->
        <#--", ${field.propertyName}=" + ${field.propertyName} +-->
        <#--</#if>-->
    <#--</#list>-->
        <#--"}";-->
    <#--}-->
<#--</#if>-->
    <#---->



}

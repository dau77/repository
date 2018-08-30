<template>
    <div class="FieldAdd">
        <fy-form :model="fieldInfo" :rules="rules" ref="form">

<#list fields as field>
            <fy-form-item :label="fields.${field.fieldEnname}.field_name" prop="table_name">
                <fy-input type="text" v-model.trim="fieldInfo.${field.fieldEnname}" :placeholder="fields.${field.fieldEnname}.field_placeholder"></fy-input>
            </fy-form-item>
</#list>
aaaaaaaaaaaaaaaa
    bbbbbb

            <fy-form-item label="语言">
                <fy-select v-model="fieldInfo.locale" :options="languages" labelKey="key_name" valueKey="key_value" @change="getFieldById"></fy-select>
            </fy-form-item>
            <fy-form-item :label="fields.table_name.field_name" prop="table_name">
                <fy-input type="text" v-model.trim="fieldInfo.table_name" :placeholder="fields.table_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.table_field.field_name" prop="table_field">
                <fy-input type="text" v-model.trim="fieldInfo.table_field" :placeholder="fields.table_field.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.page_name.field_name" prop="page_name">
                <fy-input type="text" v-model.trim="fieldInfo.page_name" :placeholder="fields.page_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.page_enname.field_name" prop="page_enname">
                <fy-input type="text" v-model.trim="fieldInfo.page_enname" :placeholder="fields.page_enname.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_name.field_name" :errorStyleType="3" prop="field_name">
                <fy-input type="text" v-model.trim="fieldInfo.field_name" :placeholder="fields.field_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_enname.field_name" prop="field_enname">
                <fy-input type="text" v-model.trim="fieldInfo.field_enname" :placeholder="fields.field_enname.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_length.field_name" prop="field_length">
                <fy-input type="text" v-model.trim="fieldInfo.field_length" :placeholder="fields.field_length.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_type.field_name" prop="field_type">
                <fy-select v-model="fieldInfo.field_type" :options="field_type" labelKey="key_name" valueKey="key_value"></fy-select>
            </fy-form-item>
            <fy-form-item :label="fields.field_type_param.field_name" prop="field_type_param">
                <fy-input type="text" v-model.trim="fieldInfo.field_type_param" :placeholder="fields.field_type_param.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_order_by.field_name" prop="field_order_by">
                <fy-input type="text" v-model.trim="fieldInfo.field_order_by" :placeholder="fields.field_order_by.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_placeholder.field_name" prop="field_placeholder">
                <fy-input type="text" v-model.trim="fieldInfo.field_placeholder" :placeholder="fields.field_placeholder.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.page_entity.field_name" prop="page_entity">
                <fy-input type="text" v-model.trim="fieldInfo.page_entity" :placeholder="fields.page_entity.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_rules.field_name" prop="field_rules">
                <fy-input type="textarea" v-model.trim="fieldInfo.field_rules" :placeholder="fields.field_rules.field_placeholder"></fy-input>
            </fy-form-item>
            <div class="form-submit">
                <input type="button" v-show="!isEdit" value="添加" @click="add">
                <input type="button" v-show="isEdit" value="修改" @click="update">
                <input type="button" value="重置" @click="reset">
                <input type="button" v-show="isEdit" value="返回" @click="returnList">
            </div>
        </fy-form>
    </div>
</template>
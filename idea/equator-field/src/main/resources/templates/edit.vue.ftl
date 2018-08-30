<template>
    <div class="${pageEnName}">
        <div>${fields[0].pageName}</div>
        <fy-form :model="dataInfo" :rules="rules" ref="form">
    <#list fields as field>
        <#if field.fieldType == 3>
            <fy-form-item :label="fields.${field.fieldEnname}.field_name" prop="${field.fieldEnname}">
                <fy-select v-model="dataInfo.${field.fieldEnname}" :options="${field.fieldEnname}" labelKey="key_name" valueKey="key_value"></fy-select>
            </fy-form-item>
        <#elseif field.fieldType == 4>
            <fy-form-item :label="fields.${field.fieldEnname}.field_name" prop="${field.fieldEnname}">
                <fy-input type="textarea" v-model.trim="dataInfo.${field.fieldEnname}" :placeholder="fields.${field.fieldEnname}.field_placeholder"></fy-input>
            </fy-form-item>
        <#else>
            <fy-form-item :label="fields.${field.fieldEnname}.field_name" prop="${field.fieldEnname}">
                <fy-input type="text" v-model.trim="dataInfo.${field.fieldEnname}" :placeholder="fields.${field.fieldEnname}.field_placeholder"></fy-input>
            </fy-form-item>
        </#if>
    </#list>
        <div class="form-submit">
            <input type="button" v-show="!isEdit" value="添加" @click="add">
            <input type="button" v-show="isEdit" value="修改" @click="update">
            <input type="button" v-show="isEdit" value="转抄" @click="transfer">
            <input type="button" value="重置" @click="reset">
            <input type="button" v-show="isEdit" value="返回" @click="returnList">
        </div>
    </fy-form>
    </div>
</template>
<script>
    import {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio, FyInputDate} from "../../components/Form"
    import pageField from "../../api/field"
    import messages from "../../api/messages"
    import {get,post,put} from "../../api/base"
    export default {
        name:"${pageEnName}",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return {
                // 页面表单字段数据
                fields:{
                <#list fields as field>
                    ${field.fieldEnname}:{},
                </#list>
                },
                // 页面表单数据
                dataInfo:{
            <#list fields as field>
                <#if field.fieldEnname == 'field_length'>
                    ${field.fieldEnname}:'30',
                <#elseif field.fieldEnname == 'field_type'>
                    ${field.fieldEnname}:'1',
                <#elseif field.fieldEnname == 'field_order_by'>
                    ${field.fieldEnname}:'10',
                <#else>
                    ${field.fieldEnname}:'',
                </#if>
            </#list>
                    ${idKeyFieldName}:'',  // id
                },
                // 验证规则
                rules: {
            <#list fields as field>
                <#if field.fieldRules != ''>
                    ${field.fieldEnname}: [{}],
                </#if>
            </#list>
                },
                // 生成 category配置数据结构
        <#list fields as field>
            <#if field.fieldType == 3 || field.fieldType == 5 || field.fieldType == 6>
                ${field.fieldEnname}: [{key_name:'', key_value:''}],
            </#if>
        </#list>

            }
        },
        // watch默认只监控一层的数据变化，深度监控
        // 默认写成函数，就相当于默认写了个handler ，deep:false
        watch: {
            dataInfo:{
                handler(){
                    // sessionStorage / localStroage 默认存的是字符串
                    sessionStorage.setItem('dataInfo', JSON.stringify(this.dataInfo));
                },
                deep:true // 深度监控
            }
        },
        // 计算属性
        computed:{
            isEdit(){
                return !(this.dataInfo.${idKeyFieldName} == '' || this.dataInfo.${idKeyFieldName} == undefined);
            },
        },
        created () { // 初始化数据
            // (如果不放缓存中，刷新后，页面上手动填写的内容会丢失)从缓存中取数据， 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            this.dataInfo = JSON.parse(sessionStorage.getItem('dataInfo')) || this.dataInfo;


            if(this.dataInfo.${idKeyFieldName} == '' || this.dataInfo.${idKeyFieldName} == undefined) {
                var fieldId = this.$route.query.fieldId
                if(fieldId != undefined) {
                    this.dataInfo.${idKeyFieldName} = fieldId
                    this.getInfoById();
                }
            }

            // 初始化fields数据，验证规则、category配置数据
            pageField.init(this, 'fields', '${pageEnName}');
        },
        // 在模板渲染成html后调用，初始化页面完成后，再对html的dom节点进行一些需要的操作
        mounted() {},
        // 实例销毁之前调用。在这一步，实例仍然完全可用  该钩子在服务器端渲染期间不被调用
        beforeDestroy(){
            // 先重置dataInfo,再放到缓存中    防止从fieldList再次进入时，dataInfo还是上一次缓存的数据
            this.reset();
            sessionStorage.setItem('dataInfo', JSON.stringify(this.dataInfo));
        },
        // Vue实例销毁后调用。调用后，Vue实例指示的所有东西都会解绑定，所有的事件监听器会被移除，所有的子实例也会被销毁 该钩子在服务器端渲染期间不被调用
        destroyed(){},
        methods: {
            // 添加
            add(){
                var validateMsg = this.validate()
                if (!validateMsg.valid) {
                    this.$message.error(validateMsg.Fields[0].message);
                    return
                }
                var url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}'
                var params = this.dataInfo
                post(url, params).then(res => {
                    //this.$router.push({name: 'FieldList'});
                    this.dataInfo = res.response.data.data
                    this.$message.success(res.message);
                }
            ).catch(err => {
                    console.log('err1:'+err);
                //const {type,message} = messages.message(err, '抱歉，添加失败！');
                //this.$message.error(message);
                this.$message.error('[' + err.status + ']' + err.message);
            });
            },
            // 修改
            update(){
                var url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}'
                var params = this.dataInfo
                put(url, params).then(res => {
                    //this.$router.push({name: 'FieldList'});
                        this.$message.success(res.message);
                    }
                ).catch(err => {
                    console.log("err:" + err.message);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            // 根据id获取信息
            getInfoById(){
                if(this.dataInfo.${idKeyFieldName} == '') {
                    return;
                }
                const url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}/' + this.dataInfo.${idKeyFieldName}
                const params = {}
                var fields = get(url, params)
                fields.then(res => {
                    this.dataInfo = res.response.data.data
                }
                ).catch(err => {
                    console.log("err:" + err);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            // 返回
            returnList(){this.$router.push({name: '${entity}List'})},
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            reset(){ // 重置数据
                this.$refs.form.resetFields(); // 只会把绑定到form里面显示了的数据清除，如果没有绑定到form里面，就不会清除，如：field_id、locale
                this.dataInfo.${idKeyFieldName} = ''
            },
            transfer(){
                this.dataInfo.${idKeyFieldName} = ''
            },
        }

    }
</script>
<template>
    <div class="fieldAdd">
        <fy-form :model="dataInfo" :rules="rules" ref="form">
            <fy-form-item label="语言">
                <fy-select v-model="dataInfo.locale" :options="languages" labelKey="key_name" valueKey="key_value" @change="getInfoById"></fy-select>
            </fy-form-item>
            <fy-form-item :label="fields.field_name.field_name" prop="field_name">
                <fy-input type="text" v-model.trim="dataInfo.field_name" :placeholder="fields.field_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_enname.field_name" prop="field_enname">
                <fy-input type="text" v-model.trim="dataInfo.field_enname" :placeholder="fields.field_enname.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_length.field_name" prop="field_length">
                <fy-input type="text" v-model.trim="dataInfo.field_length" :placeholder="fields.field_length.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_type.field_name" prop="field_type">
                <fy-select v-model="dataInfo.field_type" :options="field_type" labelKey="key_name" valueKey="key_value"></fy-select>
            </fy-form-item>
            <fy-form-item :label="fields.field_type_param.field_name" prop="field_type_param">
                <fy-input type="text" v-model.trim="dataInfo.field_type_param" :placeholder="fields.field_type_param.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_order_by.field_name" prop="field_order_by">
                <fy-input type="text" v-model.trim="dataInfo.field_order_by" :placeholder="fields.field_order_by.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_placeholder.field_name" prop="field_placeholder">
                <fy-input type="text" v-model.trim="dataInfo.field_placeholder" :placeholder="fields.field_placeholder.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.field_rules.field_name" prop="field_rules">
                <fy-input type="textarea" v-model.trim="dataInfo.field_rules" :placeholder="fields.field_rules.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.table_name.field_name" prop="table_name">
                <fy-input type="text" v-model.trim="dataInfo.table_name" :placeholder="fields.table_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.table_field.field_name" prop="table_field">
                <fy-input type="text" v-model.trim="dataInfo.table_field" :placeholder="fields.table_field.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.page_name.field_name" prop="page_name">
                <fy-input type="text" v-model.trim="dataInfo.page_name" :placeholder="fields.page_name.field_placeholder"></fy-input>
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
<script>
    import {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio, FyInputDate} from "../../components/Form"
    import pageField from "../../api/field"
    import messages from "../../api/messages"
    import {get,post,put} from "../../api/base"
    export default {
        name:"fieldAdd",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return {
                fields:{
                        field_name:{},
                        field_enname:{},
                        field_length:{},
                        field_type:{},
                        field_type_param:{},
                        field_order_by:{},
                        field_placeholder:{},
                        field_rules:{},
                        table_name:{},
                        table_field:{},
                        page_name:{},
                },
                dataInfo:{
                            field_name:'',
                            field_enname:'',
                            field_length:'30',
                            field_type:'1',
                            field_type_param:'',
                            field_order_by:'10',
                            field_placeholder:'',
                            field_rules:'',
                            table_name:'',
                            table_field:'',
                            page_name:'',
                        
                },
                rules: {
                            field_name: [{}],
                            field_enname: [{}],
                            field_length: [{}],
                            field_order_by: [{}],
                            field_rules: [{}],
                            table_name: [{}],
                            table_field: [{}],
                            page_name: [{}],
                },
                // 生成 category配置数据结构
                field_type: [{key_name:'', key_value:''}],
                languages:pageField.getLanguages(),
                isEdit:false,

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

        },
        created () { // 初始化数据
            // (如果不放缓存中，刷新后，页面上手动填写的内容会丢失)从缓存中取数据， 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            this.dataInfo = JSON.parse(sessionStorage.getItem('dataInfo')) || this.dataInfo;
            // 修改数据是否需要？


            // 初始化fields数据，验证规则、category配置数据
            pageField.init(this, 'fields', 'fieldAdd');
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
            add(){ // 添加

            },
            update(){ // 修改

            },
            // 根据id获取信息
            getInfoById(){},
            // 返回
            returnList(){},
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            reset(){ // 重置数据
                this.$refs.form.resetFields(); // 只会把绑定到form里面显示了的数据清除，如果没有绑定到form里面，就不会清除，如：field_id、locale

            },
        },
    }
</script>
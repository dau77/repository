<template>
    <div class="costInfo">
        <div>成本版本</div>
        <fy-form :model="dataInfo" :rules="rules" ref="form">
            <fy-form-item :label="fields.cost_info_no.field_name" prop="cost_info_no">
                <fy-input type="text" v-model.trim="dataInfo.cost_info_no" :placeholder="fields.cost_info_no.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_table.field_name" prop="plan_table">
                <fy-input type="text" v-model.trim="dataInfo.plan_table" :placeholder="fields.plan_table.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_id.field_name" prop="plan_id">
                <fy-input type="text" v-model.trim="dataInfo.plan_id" :placeholder="fields.plan_id.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_no.field_name" prop="plan_no">
                <fy-input type="text" v-model.trim="dataInfo.plan_no" :placeholder="fields.plan_no.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.version_remark.field_name" prop="version_remark">
                <fy-input type="textarea" v-model.trim="dataInfo.version_remark" :placeholder="fields.version_remark.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.total_pay.field_name" prop="total_pay">
                <fy-input type="text" v-model.trim="dataInfo.total_pay" :placeholder="fields.total_pay.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.total_paid.field_name" prop="total_paid">
                <fy-input type="text" v-model.trim="dataInfo.total_paid" :placeholder="fields.total_paid.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.booked_num.field_name" prop="booked_num">
                <fy-input type="text" v-model.trim="dataInfo.booked_num" :placeholder="fields.booked_num.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.version_name.field_name" prop="version_name">
                <fy-input type="text" v-model.trim="dataInfo.version_name" :placeholder="fields.version_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.who_add.field_name" prop="who_add">
                <fy-input type="text" v-model.trim="dataInfo.who_add" :placeholder="fields.who_add.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.add_time.field_name" prop="add_time">
                <fy-input type="text" v-model.trim="dataInfo.add_time" :placeholder="fields.add_time.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.editor.field_name" prop="editor">
                <fy-input type="text" v-model.trim="dataInfo.editor" :placeholder="fields.editor.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.modify_time.field_name" prop="modify_time">
                <fy-input type="text" v-model.trim="dataInfo.modify_time" :placeholder="fields.modify_time.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.check_time.field_name" prop="check_time">
                <fy-input type="text" v-model.trim="dataInfo.check_time" :placeholder="fields.check_time.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.is_last_version.field_name" prop="is_last_version">
                <fy-input type="text" v-model.trim="dataInfo.is_last_version" :placeholder="fields.is_last_version.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.cost_info_status.field_name" prop="cost_info_status">
                <fy-input type="text" v-model.trim="dataInfo.cost_info_status" :placeholder="fields.cost_info_status.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.accounting_date.field_name" prop="accounting_date">
                <fy-input type="text" v-model.trim="dataInfo.accounting_date" :placeholder="fields.accounting_date.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_name.field_name" prop="plan_name">
                <fy-input type="text" v-model.trim="dataInfo.plan_name" :placeholder="fields.plan_name.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_name_for_short.field_name" prop="plan_name_for_short">
                <fy-input type="text" v-model.trim="dataInfo.plan_name_for_short" :placeholder="fields.plan_name_for_short.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_time_1.field_name" prop="plan_time_1">
                <fy-input type="text" v-model.trim="dataInfo.plan_time_1" :placeholder="fields.plan_time_1.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.op_loc.field_name" prop="op_loc">
                <fy-input type="text" v-model.trim="dataInfo.op_loc" :placeholder="fields.op_loc.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.plan_time_2.field_name" prop="plan_time_2">
                <fy-input type="text" v-model.trim="dataInfo.plan_time_2" :placeholder="fields.plan_time_2.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.finally_flag.field_name" prop="finally_flag">
                <fy-input type="text" v-model.trim="dataInfo.finally_flag" :placeholder="fields.finally_flag.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.op.field_name" prop="op">
                <fy-input type="text" v-model.trim="dataInfo.op" :placeholder="fields.op.field_placeholder"></fy-input>
            </fy-form-item>
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
        name:"costInfo",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return {
                // 页面表单字段数据
                fields:{
                    cost_info_no:{},
                    plan_table:{},
                    plan_id:{},
                    plan_no:{},
                    version_remark:{},
                    total_pay:{},
                    total_paid:{},
                    booked_num:{},
                    version_name:{},
                    who_add:{},
                    add_time:{},
                    editor:{},
                    modify_time:{},
                    check_time:{},
                    is_last_version:{},
                    cost_info_status:{},
                    accounting_date:{},
                    plan_name:{},
                    plan_name_for_short:{},
                    plan_time_1:{},
                    op_loc:{},
                    plan_time_2:{},
                    finally_flag:{},
                    op:{},
                },
                // 页面表单数据
                dataInfo:{
                    cost_info_no:'',
                    plan_table:'',
                    plan_id:'',
                    plan_no:'',
                    version_remark:'',
                    total_pay:'',
                    total_paid:'',
                    booked_num:'',
                    version_name:'',
                    who_add:'',
                    add_time:'',
                    editor:'',
                    modify_time:'',
                    check_time:'',
                    is_last_version:'',
                    cost_info_status:'',
                    accounting_date:'',
                    plan_name:'',
                    plan_name_for_short:'',
                    plan_time_1:'',
                    op_loc:'',
                    plan_time_2:'',
                    finally_flag:'',
                    op:'',
                    cost_info_id:'',  // id
                },
                // 验证规则
                rules: {
                },
                // 生成 category配置数据结构

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
                return !(this.dataInfo.cost_info_id == '' || this.dataInfo.cost_info_id == undefined);
            },
        },
        created () { // 初始化数据
            // (如果不放缓存中，刷新后，页面上手动填写的内容会丢失)从缓存中取数据， 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            this.dataInfo = JSON.parse(sessionStorage.getItem('dataInfo')) || this.dataInfo;


            if(this.dataInfo.cost_info_id == '' || this.dataInfo.cost_info_id == undefined) {
                var fieldId = this.$route.query.fieldId
                if(fieldId != undefined) {
                    this.dataInfo.cost_info_id = fieldId
                    this.getInfoById();
                }
            }

            // 初始化fields数据，验证规则、category配置数据
            pageField.init(this, 'fields', 'costInfo');
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
                var url = 'cost_info'
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
                var url = 'cost_info'
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
                if(this.dataInfo.cost_info_id == '') {
                    return;
                }
                const url = 'cost_info/' + this.dataInfo.cost_info_id
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
            returnList(){this.$router.push({name: 'CostInfoList'})},
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            reset(){ // 重置数据
                this.$refs.form.resetFields(); // 只会把绑定到form里面显示了的数据清除，如果没有绑定到form里面，就不会清除，如：field_id、locale
                this.dataInfo.cost_info_id = ''
            },
            transfer(){
                this.dataInfo.cost_info_id = ''
            },
        }

    }
</script>
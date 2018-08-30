<template>
    <div class="FieldAdd">
        <el-form ref="fieldInfo" :rules="rules" :model="fieldInfo" label-width="140px">
            <el-row class="tac">
                <el-col :span="8">
                    <el-form-item :label="fields.field_name.field_name" prop="field_name">
                        <el-input v-model="fieldInfo.field_name"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.field_enname.field_name" prop="field_enname">
                        <el-input v-model="fieldInfo.field_enname"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.field_length.field_name" prop="field_length">
                        <el-input v-model="fieldInfo.field_length"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row class="tac">
                <el-col :span="8">
                    <el-form-item :label="fields.field_type.field_name" prop="field_type">
                        <el-select v-model="fieldInfo.field_type" size="medium" >
<el-option v-for="field in field_type" :key="field.key_value" :label="field.key_name" :value="field.key_value" >
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.field_type_param.field_name" prop="field_type_param">
                        <el-input v-model="fieldInfo.field_type_param"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.field_order_by.field_name" prop="field_order_by">
                        <el-input v-model="fieldInfo.field_order_by"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item :label="fields.field_placeholder.field_name" prop="field_placeholder">
                        <el-input v-model="fieldInfo.field_placeholder"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.table_name.field_name" prop="table_name">
                        <el-input v-model="fieldInfo.table_name"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item :label="fields.table_field.field_name" prop="table_field">
                        <el-input v-model="fieldInfo.table_field"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item :label="fields.page_name.field_name" prop="page_name">
                        <el-input v-model="fieldInfo.page_name"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :label="fields.page_entity.field_name" prop="page_entity">
                        <el-input v-model="fieldInfo.page_entity"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-form-item :label="fields.field_rules.field_name" prop="field_rules">
                        <el-input type="textarea" v-model="fieldInfo.field_rules"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item>
                <el-button v-show="!isEdit" type="primary" @click="add">添加</el-button>
                <el-button v-show="isEdit" type="primary" @click="update">修改</el-button>
                <el-button @click="cancel">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import FyForm from "../../components/FyForm"
    import FyFormItem from "../../components/FyFormItem"
    export default{
        name:"FieldAdd",
        watch: {
          //在input子组件中不能直接改变在外部双向绑定的value
          value(value) {
            console.log('watch:'+watch);
            //debugger;
            //this.defaultActive = '1';
          }
        },
        data(){
            return{
                fields:{
                    field_id:{}, 
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
                    page_entity:{} 
                },
                fieldInfo:{
                    field_id:'', // 表单字段ID
                    field_name:'', // 表单字段名称
                    field_enname:'', // 表单字段英文名
                    field_length:'', // 表单字段长度
                    field_type:'1', // 表单字段类型 1 文本框，2密码框，3 下拉开， 4 文本域， 5 单选，6 多选， 7 隐藏， 8 按钮，9 提交按钮
                    field_type_param:'', // 表单字段类型参数
                    field_order_by:'', // 表单字段排序
                    field_placeholder:'', // 表单字段占位符
                    field_rules:'', // 表单字段验证规则
                    table_name:'', // 对应表名
                    table_field:'', // 对应表字段
                    page_name:'', // 页面名称
                    page_entity:'' // 页面实体
                },
                rules: {
                    field_name: [{}],
                    field_enname: [{}],
                    field_length: [{}],
                    field_order_by:[{}]
                },
                field_type:[{key_name:'', key_value:''}],
                isEdit:false
            }
        },
        created () {
            this.$store.state.adminleftnavnum="1";
        },
        mounted : function() {
            var fieldId = this.$route.query.fieldId
            if(fieldId != undefined) {
                console.log("编辑进入fieldId"+ fieldId)
                this.isEdit = true
                this.$axios({ 
                    method:'get',
                    url:'http://192.168.11.93:18080/field/' + fieldId
                }).then(
                    res => {
                        this.fieldInfo = res.data.field
                        this.fieldInfo.field_length = this.fieldInfo.field_length + ''
                        this.fieldInfo.field_order_by = this.fieldInfo.field_order_by + ''
                        console.log("res:" + res);
                    }
                ).catch(
                    err => {
                        console.log("err:" + err);
                    }
                );
            }

            this.$axios.get('http://192.168.11.93:18080/page_field/fieldAdd', {
              　　params: {
                }}
            ).then(
                res => {
                    this.fields = res.data.field_map;

                    for(var key in this.fields){
                        var value =  this.fields[key];
                        if(value.field_rules != null && value.field_rules != undefined && value.field_rules != '') {
                            var rulelist = JSON.parse(value.field_rules).rule_list
                            for(var n = 0; n < rulelist.length; n++) { // 处理自定义验证方法
                                if(rulelist[n].hasOwnProperty("validator")) {
                                    rulelist[n].validator = this[rulelist[n].validator];
                                }
                            }
                            this.rules[value.field_enname] =  rulelist;
                        }
                        if(value.category_group != null && value.category_group != undefined) {
                            this[value.field_enname] = value.category_group;
                        }
                    }
                }
            ).catch(
                err => {
                    console.log('err:' + err);
                }
            );
        },
        methods: {
            add(){
                this.$axios({ //axios
                  data: JSON.stringify(this.fieldInfo),
                  headers: {
                    'Content-type': 'application/json;charset=UTF-8',
                  },
                  method: 'post',
                  url: 'http://192.168.11.93:18080/field'
                })  
                .then(
                    res => {
                        
                        //this.$router.push({path: 'FieldList'});
                        //debugger;
                        console.log('res:'+res);
                        this.$message({
                            message: '恭喜你，添加成功',
                            type: 'success'
                        });
                    }
                ).catch(
                    err => {
                        console.log('err:'+err);
                        //debugger;
                        var msg = err.response.data.messages[0].msg;
                        //errdata.server_id   status   rcode
                        this.$message.error(msg);
                    }
                );
            },
            update() {
                this.$axios(
                    {
                        data: JSON.stringify(this.fieldInfo),
                        method:'put',
                        headers: {'Content-type': 'application/json;charset=UTF-8'},
                        url:'http://192.168.11.93:18080/field'
                    }
                )    
                .then(
                    res => {
                        console.log("success:" + res.data);
                        console.log('status:' + res.status);
                    }  
                ).catch(
                    err => {
                        console.log("err:" + err);
                        const {type,msg} = this.message(err, '修改失败');
                        //this.showNotification('red', msg, 'warning');
                    }
                );
            },
            cancel() {
                for(var key in this.fieldInfo){
                    this.fieldInfo[key] = ''
                }
                this.isEdit = false
            },
            message(resp, defaultMsg) {
                let success = resp.status >= 200 && resp.status < 300,
                  type = success ? 'success' : 'failure',
                  msgSize = resp.data && resp.data.messages && resp.data.messages.length,
                  msg = msgSize ? resp.data.messages[msgSize - 1].msg : defaultMsg;
                  msg = msg || (success ? '操作成功' : '操作失败');
                return {type: type, message: msg};
            }
        },
        components: {
            FyForm
        }
    }
</script>
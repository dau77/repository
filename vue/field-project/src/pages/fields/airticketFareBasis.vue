<template>
    <div class="airticket_fare">
        <div>机票基础票价</div>
        <fy-form :model="dataInfo" :rules="rules" ref="form">
            <fy-form-item :label="fields.from_airport.field_name" prop="from_airport">
                <fy-input type="text" v-model.trim="dataInfo.from_airport" :placeholder="fields.from_airport.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.dest_airport.field_name" prop="dest_airport">
                <fy-input type="text" v-model.trim="dataInfo.dest_airport" :placeholder="fields.dest_airport.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.mileage.field_name" prop="mileage">
                <fy-input type="text" v-model.trim="dataInfo.mileage" :placeholder="fields.mileage.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.fare_y.field_name" prop="fare_y">
                <fy-input type="text" v-model.trim="dataInfo.fare_y" :placeholder="fields.fare_y.field_placeholder"></fy-input>
            </fy-form-item>
            <fy-form-item :label="fields.modified_time.field_name" prop="modified_time">
                <fy-input type="text" v-model.trim="dataInfo.modified_time" :placeholder="fields.modified_time.field_placeholder"></fy-input>
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
        name:"airticket_fare",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return {
                // 页面表单字段数据
                fields:{
                    from_airport:{},
                    dest_airport:{},
                    mileage:{},
                    fare_y:{},
                    modified_time:{},
                },
                // 页面表单数据
                dataInfo:{
                    from_airport:'',
                    dest_airport:'',
                    mileage:'',
                    fare_y:'',
                    modified_time:'',
                    airticket_fare_basis_id:'',  // id
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
                return !(this.dataInfo.airticket_fare_basis_id == '' || this.dataInfo.airticket_fare_basis_id == undefined);
            },
        },
        created () { // 初始化数据
            // (如果不放缓存中，刷新后，页面上手动填写的内容会丢失)从缓存中取数据， 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            this.dataInfo = JSON.parse(sessionStorage.getItem('dataInfo')) || this.dataInfo;


            if(this.dataInfo.airticket_fare_basis_id == '' || this.dataInfo.airticket_fare_basis_id == undefined) {
                var fieldId = this.$route.query.fieldId
                if(fieldId != undefined) {
                    this.dataInfo.airticket_fare_basis_id = fieldId
                    this.getInfoById();
                }
            }

            // 初始化fields数据，验证规则、category配置数据
            pageField.init(this, 'fields', 'airticket_fare');
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
                var url = 'airticket_fare_basis'
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
                var url = 'airticket_fare_basis'
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
                if(this.dataInfo.airticket_fare_basis_id == '') {
                    return;
                }
                const url = 'airticket_fare_basis/' + this.dataInfo.airticket_fare_basis_id
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
            returnList(){this.$router.push({name: 'AirticketFareBasisList'})},
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            reset(){ // 重置数据
                this.$refs.form.resetFields(); // 只会把绑定到form里面显示了的数据清除，如果没有绑定到form里面，就不会清除，如：field_id、locale
                this.dataInfo.airticket_fare_basis_id = ''
            },
            transfer(){
                this.dataInfo.airticket_fare_basis_id = ''
            },
        }

    }
</script>
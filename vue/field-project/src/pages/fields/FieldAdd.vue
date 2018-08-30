<template>
  <div class="FieldAdd">
    <div>{{fields.page_name.page_name}}</div>
    <fy-form :model="fieldInfo" :rules="rules" ref="form">
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
        <input type="button" v-show="isEdit" value="转抄" @click="transfer">

        <input type="button" value="重置" @click="reset">
        <input type="button" v-show="isEdit" value="返回" @click="returnList">
      </div>
    </fy-form>
  </div>
</template>
<script>
    import {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio, FyInputDate} from "../../components/Form";
    import pageField from "../../api/field"
    import messages from "../../api/messages"
    import {get,post,put} from "../../api/base"
    export default{
        name:"FieldAdd",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return{
            //日期组件例子
            // birthday: 0,
            // calendar: {
            //     show: false,
            //     zero: true,
            //     date_value: [2018, 12, 13], //默认日期
            //     lunar: true, //显示农历
            // },
            isInit: false, //判断页面初始化时的异步加载是否完成（由于父子组件间异步传数据，有可能子组件mounted阶段时异步加载还未完成）
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
                    page_enname:{}, 
                    page_entity:{} 
                },
                fieldInfo:{
                    field_id:'', // 表单字段ID
                    field_name:'', // 表单字段名称
                    field_enname:'', // 表单字段英文名
                    field_length:'30', // 表单字段长度
                    field_type:'1', // 表单字段类型 1 文本框，2密码框，3 下拉开， 4 文本域， 5 单选，6 多选， 7 隐藏， 8 按钮，9 提交按钮
                    field_type_param:'', // 表单字段类型参数
                    field_order_by:'10', // 表单字段排序
                    field_placeholder:'', // 表单字段占位符
                    field_rules:'', // 表单字段验证规则
                    table_name:'', // 对应表名
                    table_field:'', // 对应表字段
                    page_name:'', // 页面英文名称
                    page_enname:'', // 页面名称
                    page_entity:'', // 页面实体
                    locale:'zh_CN'
                },
                rules: {
                    field_name: [{}],
                    field_enname: [{}],
                    field_length: [{}],
                    field_order_by:[{}]
                },
                field_type:[{key_name:'', key_value:''}],
                languages:pageField.getLanguages()
                
            }
        },
        // watch默认只监控一层的数据变化，深度监控
        // 默认写成函数，就相当于默认写了个handler ，deep:false
        watch: {
            fieldInfo:{ 
                handler(){ 
                    // sessionStorage / localStroage 默认存的是字符串
                    sessionStorage.setItem('fieldInfo', JSON.stringify(this.fieldInfo));
                },
                deep:true // 深度监控
            }
        },
        // 计算属性 有get()和set()方法组成，可以只写get(),不写set(). isEdit()默认为get方法
        computed:{ 
            isEdit(){
                return !(this.fieldInfo.field_id == '' || this.fieldInfo.field_id == undefined);
            },
        },
        //在实例初始化之后，数据观测(data observer) 和event/watcher 事件配置之前被调用
        beforeCreate(){},
        //在vue实例已经创建完成之后,模板渲染成html前调用， 组件实例创建完成，初始化属性值, 然后再渲染成视图
        //实例已完成以下的配置：数据观测(data observer)，属性和方法的运算，watch/event 事件回调。然而，挂载阶段还没开始，$el 属性目前不可见
        created () { // 初始化数据
            // 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            this.fieldInfo = JSON.parse(sessionStorage.getItem('fieldInfo')) || this.fieldInfo;
            this.$store.state.adminleftnavnum="1";
            if(this.fieldInfo.field_id == '') {
                var fieldId = this.$route.query.fieldId
                if(fieldId != undefined) {
                    this.fieldInfo.locale = this.$route.query.locale
                    this.fieldInfo.field_id = fieldId
                    this.getFieldById();
                }
            }
        pageField.init(this, 'fields', 'fieldAdd').then(() => {
            this.isInit = true //异步加载完成后设置为true
        })
        },
        // 在挂载开始之前被调用：相关的render 函数首次被调用
        // 该钩子在服务器端渲染期间不被调用。
        beforeMount(){},
        //el 被新创建的 vm.$el 替换，并挂载到实例上去之后调用该钩子。如果 root 实例挂载了一个文档内元素，当 mounted 被调用时 vm.$el 也在文档内
        // 注意 mounted 不会承诺所有的子组件也都一起被挂载。如果你希望等到整个视图都渲染完毕，可以用 vm.$nextTick 替换掉 mounted
        // 该钩子在服务器端渲染期间不被调用
        mounted() { // 在模板渲染成html后调用，初始化页面完成后，再对html的dom节点进行一些需要的操作
            
        },
        //数据更新时调用，发生在虚拟DOM 重新渲染和打补丁之前。
        //你可以在这个钩子中进一步地更改状态，这不会触发附加的重渲染过程
        beforeUpdate(){},
        /* 由于数据更改导致的虚拟DOM 重新渲染和打补丁，在这之后会调用该钩子。
        当这个钩子被调用时，组件DOM 已经更新，所以你现在可以执行依赖于DOM 的操作。然而在大多数情况下，你应该避免在此期间更改状态。如果要相应状态改变，通常最好使用计算属性或watcher 取而代之 
        注意 updated 不会承诺所有的子组件也都一起被重绘。如果你希望等到整个视图都重绘完毕，可以用 vm.$nextTick 替换掉 updated
        该钩子在服务器端渲染期间不被调用
        */
        updated(){},
        // 实例销毁之前调用。在这一步，实例仍然完全可用  该钩子在服务器端渲染期间不被调用
        beforeDestroy(){
            // 先重置fieldInfo,再放到缓存中    防止从fieldList再次进入时，fieldInfo还是上一次缓存的数据
            this.reset();
            sessionStorage.setItem('fieldInfo', JSON.stringify(this.fieldInfo));
        },
        // Vue实例销毁后调用。调用后，Vue实例指示的所有东西都会解绑定，所有的事件监听器会被移除，所有的子实例也会被销毁 该钩子在服务器端渲染期间不被调用
        destroyed(){},
        methods: {
            getFieldById() {
                //this.fieldInfo.locale = this.$route.query.locale
                if(this.fieldInfo.field_id == '') {
                    return;
                }
                const url = 'field/' + this.fieldInfo.field_id
                const params = {locale:this.fieldInfo.locale}
                var fields = get(url, params)
                fields.then(
                    res => {
                        this.fieldInfo = res.response.data.field
                        this.fieldInfo.field_length = this.fieldInfo.field_length + ''
                        this.fieldInfo.field_order_by = this.fieldInfo.field_order_by + ''
                        this.fieldInfo.locale = this.$route.query.locale
                    }
                ).catch(err => {
                    console.log("err:" + err);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            add(){
                var validateMsg = this.validate()
                if (!validateMsg.valid) {
                    this.$message.error(validateMsg.Fields[0].message);
                    return
                }
                var url = 'field'
                var params = this.fieldInfo
                post(url, params).then(
                    res => {
                        //this.$router.push({name: 'FieldList'});
                        //this.fieldInfo = res.response.data.field
                        this.fieldInfo.field_id = res.response.data.field.field_id
                        this.$message.success(res.message);
                    }
                ).catch(err => {
                    console.log('err1:'+err);
                    //const {type,message} = messages.message(err, '抱歉，添加失败！');
                    //this.$message.error(message);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            returnList(){
                this.$router.push({name: 'FieldList'});
            },
            update() {

                var url = 'field'
                var params = this.fieldInfo
                put(url, params).then(
                    res => {
                        //this.$router.push({name: 'FieldList'});
                        this.$message.success(res.message);
                    }
                ).catch(err => {
                    console.log("err:" + err.message);
                    //const {type,message} = messages.message(err, '很遗憾，修改失败！');
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            reset(){
                this.$refs.form.resetFields(); // 只会把绑定到form里面显示了的数据清除，如果没有绑定到form里面，就不会清除，如：field_id、locale
                for(var key in this.fieldInfo){
                    if(key == "field_type") {
                        this.fieldInfo[key] = '1'
                    }else if (key == "field_length") {
                        this.fieldInfo[key] = '30'
                    }else if (key == "field_order_by") {
                        this.fieldInfo[key] = '10'
                    }else if(key == "locale"){
                        this.fieldInfo[key] = 'zh_CN'
                    }else {
                        this.fieldInfo[key] = ''
                    }
                }
            },
            transfer(){
                this.fieldInfo.field_id = ''
            },
            
        },
    }
</script>
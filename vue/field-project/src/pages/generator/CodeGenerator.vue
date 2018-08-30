<template>
  <div class="CodeGenerator">
    <fy-form :model="fieldInfo" :rules="rules" ref="form" v-if="isInit">
      <fy-form-item :label="fields.author.field_name" prop="author">
        <fy-input type="text" v-model.trim="fieldInfo.author" :placeholder="fields.author.field_placeholder"></fy-input>
      </fy-form-item>
       <fy-form-item :label="fields.front_end_output_dir.field_name" prop="front_end_output_dir">
        <fy-input type="text" v-model.trim="fieldInfo.front_end_output_dir" :placeholder="fields.front_end_output_dir.field_placeholder"></fy-input>
      </fy-form-item>
      <fy-form-item :label="fields.back_end_output_dir.field_name" prop="back_end_output_dir">
        <fy-input type="text" v-model.trim="fieldInfo.back_end_output_dir" :placeholder="fields.back_end_output_dir.field_placeholder"></fy-input>
      </fy-form-item>
      <fy-form-item :label="fields.file_override.field_name" prop="file_override">
        <!-- <fy-radio labelKey="l", valueKey="v", :options="options1"></fy-radio> -->
        <el-switch active-value="true" inactive-value="false" inactive-text="否" active-text="是" v-model="fieldInfo.file_override"></el-switch>
      </fy-form-item> 
      <fy-form-item :label="fields.parent_package_name.field_name" prop="parent_package_name">
        <fy-input type="text" v-model.trim="fieldInfo.parent_package_name" :placeholder="fields.parent_package_name.field_placeholder"></fy-input>
      </fy-form-item>
      <fy-form-item :label="fields.table_name.field_name" prop="table_name">
        <fy-input type="text" v-model.trim="fieldInfo.table_name" :placeholder="fields.table_name.field_placeholder"></fy-input>
      </fy-form-item>
      <fy-form-item :label="fields.module_name.field_name" prop="module_name">
        <fy-input type="text" v-model.trim="fieldInfo.module_name" :placeholder="fields.module_name.field_placeholder"></fy-input>
      </fy-form-item>

      <fy-form-item :label="fields.page_map.field_name + '【 edit 】'" prop="fieldInfo.page_map">
        <fy-input type="text" v-model.trim="fieldInfo.page_map.edit[0]" placeholder="pageName"></fy-input>
        <fy-input type="text" v-model.trim="fieldInfo.page_map.edit[1]" placeholder="文件名"></fy-input>
      </fy-form-item>

      <fy-form-item :label="fields.page_map.field_name+ '【 list 】'" prop="fieldInfo.page_map">
        <fy-input type="text" v-model.trim="fieldInfo.page_map.list[0]" placeholder="pageName"></fy-input>
        <fy-input type="text" v-model.trim="fieldInfo.page_map.list[1]" placeholder="文件名"></fy-input>
      </fy-form-item>

      <div class="form-submit">
        <input type="button" value="生成" @click="add">
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
        name:"CodeGenerator",
        components: {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio,FyInputDate},
        data(){
            return{
                isInit:false,  //判断页面初始化时的异步加载是否完成（由于父子组件间异步传数据，有可能子组件mounted阶段时异步加载还未完成）
                fields:{
                    author:{}, 
                    front_end_output_dir:{}, 
                    back_end_output_dir:{}, 
                    file_override:{}, 
                    parent_package_name:{}, 
                    table_name:{}, 
                    module_name:{},
                    page_name_map:{}, 
                    page_map:{}
                },
                fieldInfo:{
                    author:'',
                    front_end_output_dir:'G:\\vue_project\\field-project\\src',
                    back_end_output_dir:'G:\\IdeaProjects\\equator-field\\src\\main\\java',
                    file_override: false, 
                    parent_package_name: 'com.equator',
                    table_name: '',
                    module_name: '',
                    page_name_map: {},
                    page_map:{
                        edit: ['', ''],
                        list: ['', '']
                    }
                },
                rules: {
                    field_name: [{}],
                    field_enname: [{}],
                    field_length: [{}],
                    field_order_by:[{}]
                },
                // options1: [{'l':'是','v':'true'}, {'l':'否','v':'false'}]
                
            }
        },
        // watch默认只监控一层的数据变化，深度监控
        // 默认写成函数，就相当于默认写了个handler ，deep:false
        watch: {

        },
        // 计算属性
        computed:{ 

        },
        //在实例初始化之后，数据观测(data observer) 和event/watcher 事件配置之前被调用
        beforeCreate(){},
        //在vue实例已经创建完成之后,模板渲染成html前调用， 组件实例创建完成，初始化属性值, 然后再渲染成视图
        //实例已完成以下的配置：数据观测(data observer)，属性和方法的运算，watch/event 事件回调。然而，挂载阶段还没开始，$el 属性目前不可见
        created () { // 初始化数据
            // 如果sessionStroage中有数据，就用sessionStroage中的，如果没有，就用默认的
            // this.fieldInfo = JSON.parse(sessionStorage.getItem('fieldInfo')) || this.fieldInfo;
            this.$store.state.adminleftnavnum="0";
            // if(this.fieldInfo.field_id == '') {
            //     var fieldId = this.$route.query.fieldId
            //     if(fieldId != undefined) {
            //         this.fieldInfo.locale = this.$route.query.locale
            //         this.fieldInfo.field_id = fieldId
            //         this.getFieldById();
            //     }
            // }
            // this.isEdit = this.fieldInfo.field_id != ''
            pageField.init(this, 'fields', 'codeGenerator').then(() => {
                this.isInit = true;  //异步加载完成后设置为true
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
  
        },
        // Vue实例销毁后调用。调用后，Vue实例指示的所有东西都会解绑定，所有的事件监听器会被移除，所有的子实例也会被销毁 该钩子在服务器端渲染期间不被调用
        destroyed(){},
        methods: {
            validate() {  // 提交时验证
                return this.$refs.form.validate((valid, invalidFields) => {});
            },
            add(){
                var validateMsg = this.validate()
                if (!validateMsg.valid) {
                    this.$message.error(validateMsg.Fields[0].message);
                    return
                }
                var url = 'generator/all'
                var params = this.fieldInfo
                post(url, params).then(
                    res => {
                        // this.$router.push({name: 'FieldList'});
                        //const {type,message} = messages.message(res, '恭喜你，添加成功！');
                        //this.$message.success(message);
                        this.$message.success(res.message);
                    }
                ).catch(err => {
                    console.log('err1:'+err);
                    //const {type,message} = messages.message(err, '抱歉，添加失败！');
                    //this.$message.error(message);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            }
           
            
        },
    }
</script>

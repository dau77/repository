<template>
  <div class="Test">
    <!-- <fy-form ref="fieldInfo" :rules="rules" :model="fieldInfo">
      <fy-form-item lab="字段名" prop="field_name" >
        <fy-input :model="fieldInfo.field_name" placeholder="字段名1" @changeValue="zidingyi"></fy-input>
      </fy-form-item>
    </fy-form> -->
     <modal :msg="fieldInfo.message" :show="true"></modal>

    <fy-form :model="fieldInfo" :rules="rules" ref="form">
      <fy-form-item label="时间控件">
         <fy-input-date v-model="currentDate" prop=""></fy-input-date>
      </fy-form-item>
    </fy-form>
  </div>
</template>
<script>
    import {FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio, FyInputDate} from "../../components/Form";
    /* import FyForm from "../../components/FyForm"
    import FyFormItem from "../../components/FyFormItem"
    import FyInput from "../../components/FyInput" */
    import modal from "../../components/modal"
    import {getApiJson, invoke} from '../../api/base'
    const typeMap = {
        success: 'success',
        info: 'info',
        warning: 'warning',
        error: 'error'
    };
    export default{
      name:"Test",
      components: {
        //FyForm,FyFormItem,FyInput, 
        FyForm,FyFormItem,FyInput,FySelect,FyCheckbox,FyRadio, FyInputDate,modal
      },
      data(){
            return{
            currentDate:0,
            fieldInfo:{
              field_name:'',
              message:'这个消息不错呢。'
            },
            rules:{ 
              field_name:[{required: true, message: '用户名不能为空', trigger: 'blur'}]
            },
          }
      },
      mounted() { // 在模板渲染成html后调用，初始化页面完成后，再对html的dom节点进行一些需要的操作
            
        },
      methods: {
        zidingyi(value) {
          this.fieldInfo.field_name = value;
        }
      },
      
    }
</script>
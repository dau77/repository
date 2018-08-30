<template>
<div class="fy-form-div">
  <form class="fy-form">
    <slot></slot>
  </form>  
</div>
</template>
<script>
import AsyncValidator from "async-validator";
export default {
  name: "fy-form",
  componentName: "FyForm", // 通过 $options.componentName 来取
  provide() {
    return {
      fyForm: this
    };
  },
  props: {
    model: Object,
    rules: Object
  },
  data() {
    return {
      fields: [], // form下的所有formitem组件
      formError: {}
    };
  },
  created() {
    // 注册添加formitem事件
    this.$on("form.addField", field => {
      if (field) {
        this.fields.push(field);
      }
    });
    /* 注册销毁formitem事件 */
    this.$on("form.removeField", field => {
      if (field.prop) {
        this.fields.splice(this.fields.indexOf(field), 1);
      }
    });
  },
  watch:{
  },
  computed: {
    // formRules() {
    //   const descriptor = {};
    //   this.fields.forEach(({ prop }) => {
    //     if (!Array.isArray(this.rules[prop])) {
    //       console.warn(
    //         `prop 为 ${prop} 的 FormItem 校验规则不存在或者其值不是数组`
    //       );
    //       descriptor[prop] = [{ required: true }];
    //       return;
    //     }
    //     descriptor[prop] = this.rules[prop];
    //   });
    //   return descriptor;
    // },
    // formValues() {
    //   const formData = this.fields.reduce((data, { prop }) => {
    //     data[prop] = this.model[prop];
    //     return data;
    //   }, {});
    //   return formData;
    // }
  },
  methods: {
    /**
     * return:
     * array invalidFields ：数据验证失败的fields
     * boolean valid : 验证成功返回true
     */
    validate(callback) {
      const validator = new AsyncValidator(this.rules);
      if (!this.model) {
        console.warn(
          "[Element Warn][Form]model is required for validate to work!"
        );
        return;
      }
      let valid = true;
      let count = 0;
      // 如果需要验证的fields为空，调用验证时立刻返回callback
      if (this.fields.length === 0 && callback) {
        callback(true);
      }

      let invalidFields = [];
      //调用formitem的验证
      this.fields.forEach(field => {
        field.validate("", true, (message, fieldItem) => {
          if (message) {
            valid = false;
            var item = {};
            item.message = message;
            item.prop = field.prop;
            invalidFields.push(item);
          }
          if (
            typeof callback === "function" &&
            ++count === this.fields.length
          ) {
            callback(valid, invalidFields);
          }
        });
      });
      return {'valid':valid,'Fields':invalidFields};
    },
    /**
     * callback为回调函数
     */
    validateField(prop, callback) {
      let field = this.fields.filter(field => field.prop === prop)[0];
      if (!field) {
        throw new Error("must call validateField with valid prop string!");
      }
      field.validate("", true, callback);
    },
    resetFields() {
      if (!this.model) {
        console.log("prop:model不能为空");
        return;
      } else {
        this.fields.forEach(field => {
          field.resetField();
        });
      }
    }
  }
};
</script>
<style>
.fy-form-div {
  padding: 3% 2%;
  padding-right: 20%;
  text-align: center;
}
</style>


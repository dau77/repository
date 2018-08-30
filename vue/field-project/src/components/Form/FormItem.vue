<template>
  <div class="fy-form-item">
    <label v-if="label" :for="prop" class="fy-form-item-label" :class="[{'fy-required-label':requiredLabelData}]">
      {{ label }}
    </label>
    <div class="fy-form-item-content" :class="[{'fy-error-form-item-content':validateMessage},{'fy-is-success':isSuccess}]">
       <slot></slot>
       <label v-if="isShowErrot" :class="[{'fy-error-popup':styleTypeError==1 || styleTypeError==2},{'fy-error-popup-right':styleTypeError==2},{'fy-error-fixation':styleTypeError==3}]">
        {{validateMessage}}
      </label>
    </div>
  </div>
</template>
<script>
/**
 * 以下vc是vue组件的简称
 */
import AsyncValidator from "async-validator";
import util from "../utils/util.js";
export default {
  name: "fy-form-item",
  componentName: "FyFormItem",
  mixins: [util], //这些混入实例对象可以像正常的实例对象一样包含选项（可以直接this.*使用混入对象中的方法）
  provide() {
    return {
      fyFormItem: this
    };
  },
  inject: ["fyForm"],
  data() {
    return {
      isShowErrot: false,
      validateMessage: "",
      validateDisabled: false, //用于清除所有验证时做判断,执行清除操作后设置为true，不执行下一次onchange事件
      isSuccess: false,
      styleTypeError:
        this.errorStyleType === undefined || this.errorStyleType === null
          ? this.defines.errorType
          : this.errorStyleType, //错误提示样式：1:悬浮提示上方  2:悬浮提示右侧  3:固定组件下方提示
      childVc: Object, //子组件对象（componentName）
      requiredLabelData: this.requiredLabel
    };
  },
  props: {
    //设置错误提示的样式：1:悬浮提示上方  2:悬浮提示右侧  3:固定组件下方提示
    errorStyleType: {
      type: Number,
      default: null
    },
    //是否必填
    requiredLabel: {
      type: Boolean,
      default: false
    },
    label: String,
    prop: String,
    rules: [Object, Array],
    error: String
  },
  created() {
    // 注册添加formitem事件
    this.$on("formItem.addChildVc", vc => {
      if (vc) {
        this.childVc = vc;
      }
    });
  },
  mounted() {
    if (this.prop) {
      this.dispatch("FyForm", "form.addField", [this]); //发送添加formitem对象的信息，触发form中添加formitem的事件

      this.styleTypeError =
        this.childVc.$options.componentName == "FyCheckbox" ||
        this.childVc.$options.componentName == "FyRadio" ||
        this.childVc.$options.componentName == "FyInputDate"
          ? 3
          : this.styleTypeError;

      this.$on("el.form.blur", this.onFieldBlur);
      this.$on("el.form.change", this.onFieldChange);
      this.$on("el.form.focus", this.onFieldFocus);
      this.requiredLabelData = this.isRequired; //初始化是否必填的参数
    }

    // const vue = this;
    // setTimeout(function(){
    //   vue.requiredLabelData = vue.isRequired;
    // },10)
  },
  computed: {
    form() {
      //获取父级form元素
      let parent = this.$parent;
      let parentName = parent.$options.componentName;
      while (parentName !== "FyForm") {
        if (parentName === "FyFormItem") {
          this.isNested = true;
        }
        parent = parent.$parent;
        parentName = parent.$options.componentName;
      }
      return parent;
    },
    //从父级form中的model获取对应Value（根据prop）
    fieldValue: {
      cache: false, //作用计算属性不缓存,vue即将取消
      get() {
        const model = this.form.model;
        if (!model || !this.prop) {
          return;
        }

        //作用暂时不清除
        let path = this.prop;
        if (path.indexOf(":") !== -1) {
          path = path.replace(/:/, ".");
        }
        return model[this.prop];
      }
    },
    fieldError() {
      if (!this.prop) {
        return "";
      }
      const formError = this.form.formError;
      return formError[this.prop] || "";
    },
    //rule有required的判断则返回true
    isRequired() {
      let rules = this.getRules();
      let isRequired = false;

      if (rules && rules.length) {
        rules.every(rule => {
          if (rule.required && rule.required == true) {
            isRequired = true;
            return false;
          }
          return true;
        });
      }
      return isRequired;
    }
  },
  methods: {
    noop() {},
    //清除内容
    resetField() {
      this.validateDisabled = true;
      this.isSuccess = false;
      this.validateMessage = "";
      this.isShowErrot = false;
      //清空value(date组件的清空还有问题)
      this.childVc.resetField();
    },
    //验证方法
    validate(trigger, isFormSubmit, callback = this.noop) {
      this.validateDisabled = false;
      const rules = this.getFilteredRule(trigger);
      if (!rules || rules.length === 0) {
        callback();
        return true;
      }
      const descriptor = {};
      descriptor[this.prop] = rules;

      const validator = new AsyncValidator(descriptor);
      const model = {};
      model[this.prop] = this.fieldValue;
      validator.validate(model, (errors, invalidFields) => {
        if (this.styleTypeError == 3 || !isFormSubmit) {
          this.isShowErrot = !errors ? false : true;
        } else {
          this.isShowErrot = false;
        }
        //this.isShowErrot = !errors ? false : true;
        this.validateMessage = errors ? errors[0].message : "";
        if (trigger == "blur" || trigger == "change") {
          this.isSuccess = !errors ? true : false;
        }
        if (errors) {
          console.log(">>>>>>>出错啦~~>>>>>>>");
          console.log("message:" + errors[0].message);
        }
        callback(this.validateMessage, invalidFields);
        //this.fyForm && this.fyForm.$emit("validate", this.prop, !errors);
      });
    },
    //获取Rules，从父级form组件中取
    getRules() {
      let formRules = this.form.rules;
      const selfRules = this.rules;
      formRules = formRules ? formRules[this.prop] : [];
      //设置自定义验证方法
      formRules = this.customValidator(formRules,this.form.model);
      return [].concat(selfRules || formRules || []);
    },
    //过滤Rules，过滤触发类型不对应的rule
    getFilteredRule(trigger) {
      const rules = this.getRules();
      return rules.filter(rule => {
        if (!rule.trigger || trigger === "") return true;
        if (Array.isArray(rule.trigger)) {
          return rule.trigger.indexOf(trigger) > -1;
        } else {
          return rule.trigger === trigger;
        }
      });
      //.map(rule => objectAssign({}, rule));
    },
    //触发事件
    onFieldBlur(vc) {
      this.validate("blur");
      if (
        this.isShowErrot &&
        (this.styleTypeError == 1 || this.styleTypeError == 2)
      ) {
        this.isShowErrot = false;
      }
    },
    //Change触发事件还有问题
    onFieldChange(vc) {
      if (this.validateDisabled) {
        this.validateDisabled = false;
        return;
      }
      this.validate("change");
    },
    onFieldFocus(vc) {
      this.validate("focus");
    }
  },
  beforeDestroy() {
    this.dispatch("ElForm", "form.removeField", [this]);
  }
};
</script>
<style>
.fy-form-item {
  margin-bottom: 30px;
}
.fy-form-item .fy-form-item-label {
  width: 25%;
  text-align: right;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: #606266;
  line-height: 40px;
  padding: 0 12px 0 0;
  box-sizing: border-box;
  font-weight: 800;
}
.fy-form-item .fy-required-label:before {
  content: "*";
  color: #f56c6c;
  margin-right: 4px;
}
.fy-form-item .fy-form-item-content {
  line-height: 40px;
  position: relative;
  font-size: 14px;
  margin-left: 25%;
}
.fy-form-item:after,
.fy-form-item:before {
  display: table;
  content: "";
}

.fy-form-item .fy-error-form-item-content .fy-input:active,
.fy-form-item .fy-error-form-item-content .fy-input:focus,
.fy-form-item .fy-error-form-item-content .fy-select:active,
.fy-form-item .fy-error-form-item-content .fy-select:focus {
  outline-color: #e0b5b5;
  box-shadow: 0 0 3px 0 #fa0e0e;
}
.fy-form-item .fy-is-success .fy-input,
.fy-form-item .fy-is-success .fy-select {
  border: 1px solid #67c23a;
  box-shadow: 0 0 5px #67c23a;
}
.fy-form-item .fy-error-form-item-content .fy-select,
.fy-form-item .fy-error-form-item-content .fy-input {
  border: 1px solid #e0b5b5;
  box-shadow: 0 0 5px #e0b5b5;
}
.fy-form-item label.fy-error-popup {
  z-index: 10;
  transition: opacity 0.3s;
  position: absolute;
  bottom: 100%;
  left: 0;
  margin-bottom: 14px;
  padding: 0px 10px;
  height: auto;
  min-width: 90px;
  background-color: #3b3e43;
  box-shadow: 0 0 11px rgba(0, 0, 0, 0.75);
  color: #fff;
  vertical-align: middle;
  font-size: 12px;
}
.fy-form-item label.fy-error-popup:after {
  position: absolute;
  bottom: -15px;
  left: 16px;
  z-index: 10;
  content: "";
  display: block;
  width: 0;
  height: 0;
  border: 8px solid transparent;
  border-top-color: #3b3e43;
}
.fy-form-item label.fy-error-popup-right {
  bottom: -35%;
  left: 105%;
  white-space: nowrap;
}
.fy-form-item label.fy-error-popup-right:after {
  bottom: 12px;
  left: -16px;
  border: 8px solid transparent;
  border-right-color: #3b3e43;
}
.fy-form-item label.fy-error-fixation {
  color: #f56c6c;
  font-size: 12px;
  line-height: 1;
  padding-top: 4px;
  position: absolute;
  top: 100%;
  left: 0;
}
</style>



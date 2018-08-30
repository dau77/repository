<template>
<div>
    <input v-if="type !== 'textarea'" :type="type" ref="input" v-model="currentValue" :placeholder="placeholder"
    @focus="handleFocus" @blur="handleBlur" @change="handleChange" @input="handleInput"
    class="fy-input"/>

    <textarea v-else :value="currentValue" :placeholder="placeholder"
     @focus="handleFocus" @blur="handleBlur" @change="handleChange" @input="handleInput"
    class="fy-input"/>
</div>
</template>
<script>
import util from "../utils/util.js";
export default {
  name: "FyInput",

  componentName: "FyInput",
  mixins: [util], //这些混入实例对象可以像正常的实例对象一样包含选项（可以直接this.*使用混入对象中的方法）

  inject: {
    fyForm: {
      default: ""
    },
    fyFormItem: {
      default: ""
    }
  },
  data() {
    return {
      currentValue:
        this.value === undefined || this.value === null ? "" : this.value
    };
  },
  props: {
    value: [String, Number],
    type: {
      type: String,
      default: "text"
    },
    placeholder: {
      type: String,
      default: ""
    }
  },
  watch: {
    //在input子组件中不能直接改变在外部双向绑定的value
    value(value) {
      this.setCurrentValue(value);
    },
    currentValue(value) {
      this.$emit("input", value); // 触发 input 事件
      //this.$emit("change", value); // 触发 change 事件
      //this.dispatch("FyFormItem", "el.form.change", [value]); // 向父级的派发 el.form.change 事件
    }
  },
  mounted() {
    this.dispatch("FyFormItem", "formItem.addChildVc", [this]); //发送添加formitem对象的信息
  },
  methods: {
    resetField() {
      this.$emit("input", "");
    },
    setCurrentValue(value) {
      if (value === this.currentValue) return; // 如果新旧值一致直接返回
      this.currentValue = value; // 改变当前值
    },
    handleFocus(event) {
      const value = event.target.value;
      this.dispatch("FyFormItem", "el.form.focus", [value]);
    },
    handleInput(event) {
      const value = event.target.value;
      this.$emit("input", value);
      this.dispatch("FyFormItem", "el.form.change", [value]);
    },
    //Change触发事件还有问题
    handleChange(event) {
      const value = event.target.value;
      this.$emit("change", value);
      //this.dispatch("FyFormItem", "el.form.change", [value]);
    },
    handleBlur(event) {
      const value = event.target.value;
      this.dispatch("FyFormItem", "el.form.blur", [value]);
    },
    // dispatch(componentName, eventName, params) {
    //   var parent = this.$parent || this.$root;
    //   var name = parent.$options.componentName;

    //   while (parent && (!name || name !== componentName)) {
    //     parent = parent.parentNode;

    //     if (parent) {
    //       name = parent.$options.componentName;
    //     }
    //   }
    //   if (parent) {
    //     parent.$emit.apply(parent, [eventName].concat(params));
    //   }
    // }
  }
};
</script>
<style>
textarea.fy-input {
  resize: vertical;
  padding: 5px 15px;
  sline-height: 1.5;
  box-sizing: border-box;
  width: 100%;
  font-size: inherit;
  color: #606266;
  background-color: #fff;
  background-image: none;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}
input.fy-input {
  -webkit-appearance: none;
  background-color: #fff;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  height: 40px;
  line-height: 30px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
  width: 100%;
}
input.fy-input::-webkit-input-placeholder,textarea.fy-input::-webkit-input-placeholder {
  color:#DBDBDB;
}
</style>

<template>
    <select v-model="currentValue" @blur="handleBlur"  @input="handleInput" @change="handleChange"
    class="fy-select">
      <option v-for="(item,index) in options" :value="item[valueKey]" :key="index">
        {{item[labelKey]}}
      </option>
    </select>
</template>
<script>
import util from "../utils/util.js";
export default {
  name: "FySelect",
  componentName: "FySelect",
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
    options: {
      type: Array,
      default: function() {
        return [];
      }
    },
    labelKey: String,
    valueKey: String
  },
  watch: {
    //在input子组件中不能直接改变在外部双向绑定的value
    value(value) {
      this.currentValue = value;
      this.$emit("change", value);
      //this.dispatch("FyFormItem", "el.form.change", [value]);
    }
  },
  mounted(){
    this.dispatch("FyFormItem", "formItem.addChildVc", [this]); //发送添加formitem对象的信息
  },
  methods: {
    resetField(){
      this.$emit("input", '');
    },
    //以下event用元素input是没有问题传的是Event对象，但是用quasar的input组件时，传递的直接是value
    // handleFocus(event) {
    //   const value = event.target.value;
    //   this.dispatch("FyFormItem", "el.form.focus", [value]);
    // },
    handleInput(event) {
      const value = event.target.value;
      this.$emit("input", value);  //发送value的值给formitem
      //this.dispatch("FyFormItem", "el.form.change", [value]);
    },
    //Change触发事件还有问题
    handleChange(event) {
      const value = event.target.value;
      this.$emit("change", value);
      this.dispatch("FyFormItem", "el.form.change", [value]);
    },
    handleBlur(event) {
      const value = event.target.value;
      this.dispatch("FyFormItem", "el.form.blur", [value]);
    },
  }
};
</script>
<style>
button,
input,
select,
textarea {
  font-family: inherit;
  font-size: inherit;
  line-height: inherit;
  color: inherit;
}
.fy-select {
  width: 100%;
  padding: 10px 2%;
}
</style>

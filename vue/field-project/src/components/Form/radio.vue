<template>
  <div class="fy-radio">
    <span class="radio-span" v-for="(item,index) in options" :key="index">
      <input type="radio" :value="item[valueKey]" @change="handleChange" :name="name">
      <label>{{item[labelKey]}}</label>
     </span>
  </div>
</template>
<script>
import util from "../utils/util.js";
export default {
  name: "FyRadio",
  mixins: [util], //这些混入实例对象可以像正常的实例对象一样包含选项（可以直接this.*使用混入对象中的方法）
  componentName: "FyRadio",

  inject: {
    fyForm: {
      default: ""
    },
    fyFormItem: {
      default: ""
    }
  },
  data() {
    return {};
  },
  props: {
    name: String,
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
      this.$emit("change", this.value);
      this.initOptions();
    }
  },
  mounted() {
    this.dispatch("FyFormItem", "formItem.addChildVc", [this]); //发送添加formitem对象的信息
  },
  updated() {
    this.initOptions();
  },
  methods: {
    resetField(){
      this.$emit("input", '');
    },
    initOptions() {
      var ii = this.options.length;
      var radios = document.getElementsByName(this.name);
      for (var i = 0; i < this.options.length; i++) {
        radios[i].checked =
          this.value.indexOf(this.options[i][this.valueKey]) !== -1;
      }
    },
    //以下event用元素input是没有问题传的是Event对象，但是用quasar的input组件时，传递的直接是value值
    handleFocus(event) {
      this.dispatch("FyFormItem", "el.form.focus", [this.value]);
    },
    handleInput(event) {
      this.$emit("input", this.value);
    },
    //Change触发事件还有问题
    handleChange(event) {
      var value = event.target.value;
      this.$emit("change", value);
      this.$emit("input", value);
      this.dispatch("FyFormItem", "el.form.change", [value]);
    },
    handleBlur(event) {
      this.dispatch("FyFormItem", "el.form.blur", [this.value]);
    },
  }
};
</script>
<style>
.fy-radio {
  text-align: left;
}
.fy-radio .radio-span {
  margin-right: 4%;
}
</style>

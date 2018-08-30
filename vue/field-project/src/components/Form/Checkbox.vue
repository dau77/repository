<template>
  <div class="fy-checkbox">
    <span class="checkbox-span" v-for="(item,index) in options" :key="index">
      <input type="checkbox" :value="item[valueKey]" @change="handleChange" :name="name">
      <label>{{item[labelKey]}}</label>
     </span>
  </div>
</template>
<script>
import util from "../utils/util.js";
export default {
  name: "FyCheckbox",
  componentName: "FyCheckbox",
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
      checkeds: {},
      valueArray:this.value?[]:this.value.split(','),
    };
  },
  props: {
    name: String,
    value: {
      type: String,
      default: function() {
        return 'FyCheckbox';
      }
    },
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
      this.valueArray = this.value.split(',');
      this.$emit("change", this.value);
      this.initOptions();
    },
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
      var checkboxs = document.getElementsByName(this.name);
      for (var i = 0; i < this.options.length; i++) {
        checkboxs[i].checked = this.valueArray.indexOf(this.options[i][this.valueKey]) !== -1;
      }
    },
    //以下event用元素input是没有问题传的是Event对象，但是用quasar的input组件时，传递的直接是value值
    handleFocus(event) {
      this.dispatch("FyFormItem", "el.form.focus", [this.value]);
    },
    handleInput(event) {
      this.$emit("input", this.value);
    },
    handleChange(event) {
      var checked = event.target.checked;
      if (checked) {
        this.valueArray.push(event.target.value);
      } else {
        for (var i = 0; i < this.valueArray.length; i++) {
          if (this.valueArray[i] == event.target.value) {
            this.valueArray.splice(i, 1);
            break;
          }
        }
      }
      var value = this.valueArray + '';
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
.fy-checkbox {
  text-align: left;
}
.fy-checkbox .checkbox-span {
  margin-right: 4%;
}
</style>

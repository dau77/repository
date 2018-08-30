<template>
    <div class="fy_input">
        <el-input v-model="currentValue" :placeholder="placeholder" @change="handleChange">
            <slot></slot>
        </el-input>
        value:{{value}}
        currentValue:{{currentValue}}
    </div>
</template>
<script>
    export default {
        name: "fy-input",
        componentName: "FyInput",
        data() {
          return {
            currentValue:
              this.value === undefined || this.value === null ? '' : this.value
          };
        },
        props: {
          value: String,
          placeholder: String,
        },
        watch: {
          //在input子组件中不能直接改变在外部双向绑定的value
          
          value(value) {
            console.log('watch:'+watch);
            //debugger;
            //this.currentValue = value;
          }
        },
        methods:{
          handleChange(event) {
            const value = event;
            this.$emit("changeValue", value);
          }
        }
    }
</script>  
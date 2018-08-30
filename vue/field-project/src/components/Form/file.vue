<template>
    <div class="fy-file-div">
        <span>{{file_name}}</span>
        <a class="fy-file">
            选择文件
            <input type="file" ref="input" :placeholder="placeholder" @change="handleChange" />
        </a>
    </div>
</template>
<script>
import util from '../utils/util.js'
export default {
    name: 'FyFile',

    componentName: 'FyFile',
    mixins: [util], //这些混入实例对象可以像正常的实例对象一样包含选项（可以直接this.*使用混入对象中的方法）

    inject: {
        fyForm: {
            default: ''
        },
        fyFormItem: {
            default: ''
        }
    },
    data() {
        return {
            currentValue: '',
            file_name:'请选择文件'
        }
    },
    props: {
        value: '',
        //file的value值没办法赋初值，只能设置为空字符串
        placeholder: {
            type: String,
            default: ''
        }
    },
    watch: {},
    mounted() {
        this.dispatch('FyFormItem', 'formItem.addChildVc', [this]) //发送添加formitem对象的信息
    },
    methods: {
        resetField() {
            this.$emit('input', '')
        },
        handleChange(event) {
            const file = event.target.files[0]
            this.$emit('input', file)
            this.dispatch('FyFormItem', 'el.form.change', [event.target.value])
            const value = this.$refs.input.value;
            this.file_name = value.substr(value.lastIndexOf('\\')+1);
        }
    }
}
</script>
<style>
.fy-file-div{
    text-align: left;
}
.fy-file-div span{
    /* border:1px solid #dcdfe6;
    padding: 3.2% 2%;
    line-height: 28px; */
    margin-right: 3%;
}
.fy-file {
    text-align: center;
    vertical-align: middle;
    border-radius: 2px;
    -webkit-border-radius: 2px;
    -moz-border-radius: 2px;
    color: #fff;
    cursor: pointer;
    background: #4387cd;
    border: 1px solid #4387cd;
    outline: none;
    position: relative;
    left: 0;
    display: inline-block;
    overflow: hidden;
    text-decoration: none;
    line-height: 28px;
    padding: 1% 2%;
}
.fy-file input {
    position: absolute;
    font-size: 10px;
    border: none;
    cursor: pointer;
    left: 0;
    top: 0;
    opacity: 0;
}
.fy-file:hover {
    display: inline-block;
    color: #fff;
    text-decoration: none;
    cursor: pointer !important;
    opacity: 0.8;
}

.fy-file :hover {
    display: inline-block;
    overflow: hidden;
    color: #fff !important;
    cursor: pointer;
    text-decoration: none;
}
</style>

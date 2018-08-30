<template>
    <div class="fy_form_item">
        <el-form-item :label="lab" :prop="prop" >
            <slot></slot>
        </el-form-item>

        item.value:{{this.lab}}
        item.prop:{{prop}}
    </div>
</template>
<script>
    export default {
        name: "fy_form_item",
        componentName: "FyFormItem",
        data() {
            return {
                
            };
        },
        props: {
            lab: String,
            prop: String,
        },
        methods: {
            
        }
    }
</script>   
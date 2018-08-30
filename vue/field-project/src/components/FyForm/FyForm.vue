<template>
    <div class="fy_form">
        <el-form label-width="100px" :rules="rules" :model="model">
            <slot></slot>
        </el-form>
        form.rules:{{rules}}<br>
        form.model:{{model}}<br>
    </div>
</template>
<script>
    export default {
        name: "fy-form",
        componentName: "FyForm",
        data(){
            return {

            }
        },
        props:{
            rules:Object,
            model:Object
        },
    }
</script>
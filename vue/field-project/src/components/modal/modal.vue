<template>
    <div class="mask" v-show="visible">
        <div class="dialog" @click="shutdown">{{message}}</div>
        {{visible}}
    </div>
</template>
<style>
    .dialog {
        background-color: aquamarine;
        width: 30%;
        height: 45px;
        margin-top: 1px;
        position: absolute;
        top: 0;
        margin-left: 20%;
        text-align: center;
    }
</style>
<script>
export default{
    name: "modal",
    data(){
        return {
            visible: this.show,
            message: this.msg,
            duration: 300000,
            type: 'info',
            iconClass: '',
            customClass: '',
            timer: null,
        }
    },
    // watch默认只监控一层的数据变化，深度监控
    // 默认写成函数，就相当于默认写了个handler ，deep:false
    watch:{
        flag: { 
            handler(){ 
                
            },
            deep:true // 深度监控
        }
    },
    computed:{
      
    },
    mounted() { // 在模板渲染成html后调用，初始化页面完成后，再对html的dom节点进行一些需要的操作
        // setTimeout(() => {
        //     console.log('延时加载');
        //     this.shutdown();
        // }, this.duration);

        this.startTimer();
    },
    props:{
        show: {
            type:Boolean,
            default:false
        },
        msg: {
            type:[String],
            default:''
        }
    },
    methods:{
        shutdown() {
            this.visible = false;
            //this.$emit('close');
        },
        startTimer() {
            if (this.duration > 0) {
                this.timer = setTimeout(() => {
                    this.visible = false;
                }, this.duration);
            }
        },
        clearTimer() {
            clearTimeout(this.timer);
        },

    }
}
</script>
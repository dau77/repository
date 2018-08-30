// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'es6-promise/auto'
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex/store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import defines from './config'

//import Toast from './components/toast/toast'
//Vue.use(Toast);

Vue.prototype.defines = defines
Vue.config.productionTip = false // 关闭生产模式下给出的提示，另外需要将 NODE_ENV 设置为 production
    //Vue.config.debug = true //开启debug模式
Vue.use(ElementUI)
Vue.prototype.$axios = axios
axios.defaults.withCredentials = true // 允许axios跨域携带cookie信息
axios.defaults.baseURL = 'http://localhost:18081/equator-field/'
    //axios.defaults.baseURL = 'http://10.8.8.26:28080/equator-field/'
    //axios.defaults.baseURL = 'http://192.168.11.200:18080/'

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
})
import Vue from 'vue'
import axios from 'axios'
import messages from "./messages"
Vue.prototype.$axios = axios
axios.defaults.withCredentials = true // 允许axios跨域携带cookie信息
    //export const BASE_URL = 'http://192.168.11.245:18080/'
export const API_PRE = '/api/'
export const API_SUFFIX = '.vdt'


function getAuthorization() {
    return sessionStorage.getItem("token_type") + " " + sessionStorage.getItem("token");
}

// 添加请求拦截器
/* var myInterceptor = axios.interceptors.request.use(
    config => {
        const token = sessionStorage.getItem("token"); //获取存储在本地的token
        config.data = JSON.stringify(config.data);
        config.headers = {
            'Content-Type': 'application/json' //设置跨域头部,虽然很多浏览器默认都是使用json传数据，但咱要考虑IE浏览器。
        };
        if (token) {
            config.headers.Authorization = "Token " + token; //携带权限参数
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    }
); */

axios.interceptors.response.use(response => {
    // 对响应数据做点什么
    console.log('响应拦截器then:' + response.status);
    console.log('响应拦截器response.this:' + this);
    var status = response.status
    var message = messages.message(response, '恭喜你，操作成功了！');
    //debugger;
    //message = messages.message(response, '操作成功！');
    /* if (response.status && response.status == 200 && response.data.status == 'error') {
        //Message.error({ message: response.data.msg });
        return;
    } */
    return { status: status, message: message.message, response: response }
    //return response;
}, error => {
    // 对响应错误做点什么
    var message = ''
    console.log('响应拦截器err：' + error);
    console.log('响应拦截器err.this:' + this);
    //console.log('响应拦截器err：' + error.status);

    var status = error.response.status
        //var msg = error.response.data.messages[0].msg
    if (status == 504 || status == 404) {
        console.log('服务器被吃了⊙﹏⊙∥');
        //Message.error({ message: '服务器被吃了⊙﹏⊙∥' });
        message = messages.message(error.response, '操作失败,服务器被吃了！');
    } else if (status == 401) {
        console.log('token过期');
        message = messages.message(error.response, '操作失败,response.data.error！');
        location.replace('/');
        console.log('router:' + this.$router);
    } else if (status == 403) {
        console.log('权限不足,请联系管理员!');
        message = messages.message(error.response, '操作失败,权限不足,请联系管理员！');
    } else if (status == 400) {
        //console.log('错误请求!' + msg);
        message = messages.message(error.response, '操作失败,很遗憾，错误的请求!');
    } else if (status == 422) {
        console.log('语义错误,无法响应!');
        message = messages.message(error.response, '操作失败,语义错误,无法响应！');
    } else {
        //console.log('未知错误!');
        message = messages.message(error.response, '操作失败,未知错误！');
    }
    return { status: status, message: message.message }
    //return Promise.reject(error);
});


export const get = (url, args = {}) => {
    let promise = new Promise((resolve, reject) => {
        axios({
                method: 'get',
                params: args,
                headers: { 'Authorization': getAuthorization() },
                url: url
            })
            .then((res) => {
                if (res.status === 200) {
                    //console.log('res'+res);
                    resolve(res)
                } else {
                    //console.error(res)
                    reject(res)
                }
            })
            .catch((err) => {
                reject(err)
            })
    })
    return promise
}

export const deletes = (url, args = {}) => {
    let promise = new Promise((resolve, reject) => {
        axios({
                method: 'delete',
                params: args,
                headers: { 'Authorization': getAuthorization() },
                url: url
            })
            .then((res) => {
                if (res.status === 200) {
                    resolve(res)
                } else {
                    console.error(res)
                    reject(res)
                }
            })
            .catch((err) => {
                reject(err)
            })
    })
    return promise
}

export const post = function(url, args, params = {}, ignoreError) {
    let promise = new Promise((resolve, reject) => {
        axios({
                method: 'post',
                data: JSON.stringify(args),
                headers: { 'Content-type': 'application/json;charset=UTF-8', 'Authorization': getAuthorization() },
                url: url,
            })
            .then((res) => {
                if (res.status === 200) {
                    resolve(res)
                } else {
                    console.log('post.then.reject=' + res)
                    reject(res)
                }
            })
            .catch((err) => {
                console.log('post.catch.reject=' + err);
                reject(err)
            })
    })
    return promise
}

export const put = function(url, args, params = {}, ignoreError) {
    let promise = new Promise((resolve, reject) => {
        axios({
                method: 'put',
                data: JSON.stringify(args),
                headers: {
                    'Content-type': 'application/json;charset=UTF-8',
                    'Authorization': getAuthorization()
                },
                url: url,
            })
            .then((res) => {
                if (res.status === 200) {
                    //console.log('res'+res);
                    resolve(res)
                } else {
                    //console.error(res)
                    reject(res)
                }
            })
            .catch((err) => {
                reject(err)
            })
    })
    return promise
}

export const login = function(url, args, ) {
    let promise = new Promise((resolve, reject) => {
        axios({
                method: 'Post',
                data: args,
                headers: {
                    'Content-type': 'application/x-www-form-urlencoded',
                    'Authorization': 'Basic Y2xpZW50XzI6MTIzNDU2'
                },
                url: url,
            })
            .then((res) => {
                if (res.status === 200) {
                    //console.log('res'+res);
                    resolve(res)
                } else {
                    //console.error(res)
                    reject(res)
                }
            })
            .catch((err) => {
                reject(err)
            })
    })
    return promise
}
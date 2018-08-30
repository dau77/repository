import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const state = {
    count: 0,
    isLogin: 0,
    adminleftnavnum: "", //管理后台左侧导航
    //switchChecked: true, // 多语言
}
const mutations = {
    increment(state) {
        state.count++
    },
    changeLogin(state, status) {
        state.isLogin = status
    }
}

const actions = {
        loginAction({ commit }) {
            commit('changeLogin', 1);
        }
    }
    // const actions = {...}
    //注册Store
export default new Vuex.Store({
    state,
    actions,
    mutations
});
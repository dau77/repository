import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/login'
import Main from '@/pages/system/main'
import FieldAdd from '@/pages/fields/FieldAdd'
import FieldList from '@/pages/fields/FieldList'
import Test from '@/pages/fields/test'
import CostInfo from '@/pages/fields/costInfo'
import CostList from '@/pages/fields/costInfoList'
import CodeGenerator from '@/pages/generator/CodeGenerator'

import AirticketFareBasis from '@/pages/fields/airticketFareBasis'
import AirticketFareBasisList from '@/pages/fields/airticketFareBasisList'

Vue.use(Router)

/*
export default new Router({
  routes: [
    {path: '/', name: 'Login', component: Login},
    {path:'/main', name: 'Main', component: Main,
      children:[
        { path: 'fields/FieldAdd', name: 'FieldAdd', component: FieldAdd},
        { path: 'fields/FieldList', name: 'FieldList', component: FieldList},
        { path: 'fields/Test', name: 'Test', component: Test},
      ]
    }
  ]
})
*/

const routes = [
    { path: '/', name: 'Login', component: Login },
    {
        path: '/main',
        //name: 'Main', // 子路由设置了默认视图后，父路由设置name就有警告的异常
        component: Main,
        children: [
            // 设置子路由默认视图，两种写法。 
            //{ path: '', name: 'default', component: FieldList },
            { path: '', redirect: 'fields/FieldList' },
            { path: 'fields/FieldAdd', name: 'FieldAdd', component: FieldAdd },
            { path: 'fields/FieldList', name: 'FieldList', component: FieldList },
            { path: 'fields/Test', name: 'Test', component: Test },
            { path: 'fields/CostInfo', name: 'CostInfo', component: CostInfo },
            { path: 'fields/CostList', name: 'CostInfoList', component: CostList },

            { path: 'generator/CodeGenerator', name: 'CodeGenerator', component: CodeGenerator },
            { path: 'AirticketFareBasis', name: 'AirticketFareBasis', component: AirticketFareBasis },
            { path: 'AirticketFareBasisList', name: 'AirticketFareBasisList', component: AirticketFareBasisList },
        ]
    },
    { path: '/*', redirect: '/main' } // 如果在浏览器输入一个没有在这里配置过的路由，则会跳转到主页。

]


const vueRouter = new Router({
    base: __dirname,

    //mode: "history", //启用浏览器的历史记录
    //base: '/dist/',
    // mode:"hash",
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes
})

/* beforeEach  钩子实现导航守卫
 to: Route: 即将要进入的目标 路由对象
 from: Route: 当前导航正要离开的路由
 next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数
    next(): 进行管道中的下一个钩子。如果全部钩子执行完了，则导航的状态就是 confirmed （确认的）。
    next(false): 中断当前的导航。如果浏览器的 URL 改变了（可能是用户手动或者浏览器后退按钮），那么 URL 地址会重置到 from 路由对应的地址。
    next('/') 或者 next({ path: '/' }): 跳转到一个不同的地址。当前的导航被中断，然后进行一个新的导航。
    next(error): (2.4.0+) 如果传入 next 的参数是一个 Error 实例，则导航会被终止且该错误会被传递给 router.onError() 注册过的回调 */
vueRouter.beforeEach(function(to, from, next) {
    const nextRoute = ['FieldAdd', 'FieldList'] // 组件名称   跳转至这几个页面时，如果未登陆，就会跳转到登录页
    const token = sessionStorage.getItem('token')
    if (nextRoute.indexOf(to.name) >= 0) {
        if (token == null || token == "") {
            //to.fullPath:要跳转页面的路径(带参数)
            //to.path：要跳转页面的路径(不带参数)
            next({ name: 'Login', query: { to_fullPath: to.fullPath, to_path: to.path } })
        } else {
            next()
        }
    } else {
        next()
    }
})

export default vueRouter
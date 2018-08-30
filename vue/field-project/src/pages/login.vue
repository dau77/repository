<template>
    <div class="login">
        <el-form ref="userinfo" :model="userinfo" class="demo-ruleForm" label-width="100px" status-icon>
            <el-form-item label="用户名">
                <el-input v-model="userinfo.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input type="password" v-model="userinfo.password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button  type="primary" @click="login">登陆</el-button>
            </el-form-item>
        </el-form>      
    </div>
</template>
<script type="text/javascript">
import {login} from "../api/base"
export default {
    name: 'login', 
    data() {
        return{
            userinfo:{
                username: '',
                password: ''
            }
        }
    },
    methods: {
        login() {
            const Qs = require('qs');
            //this.$router.push({path: '/Main', query: {username: this.userinfo.username}});
            var url = 'oauth/token'
            var param = Qs.stringify({
                        username: this.userinfo.username,
                        password: this.userinfo.password,
                        grant_type:'password'
                    });
            login(url, param).then(
                res => {
                    console.log("success:" + res);
                      //debugger;
                    //sessionStorage.setItem('token',res.data.data.token)
                    if(res.status == 200) {
                      sessionStorage.setItem('token',res.response.data.access_token);
                      sessionStorage.setItem('token_type',res.response.data.token_type);
                      sessionStorage.setItem('username',this.userinfo.username);
//                      this.$message.success("恭喜你，登陆成功!");
                      this.$message.success('[' + res.status + ']' + res.message);
	              //1.router.currentRoute:当前的路由信息对象，我们可以通过router.currentRoute.fullPath获得解析后的 URL，包含查询参数和 hash 的完整路径，
	              //如果要访问的页面的路由有命名（name）的话，可以通过router.currentRoute.name获得当前路由的名称。
	              const currentRoute = this.$router.currentRoute;
	              //判断是否时登陆拦截过来的，时则跳回上一个页面，不是则调到首页
	              //currentRoute.query.original_hostname == location.hostname    是否加域名判断??
	              if (currentRoute.query.to_fullPath && currentRoute.query.to_path != currentRoute.path) {
	                this.$router.push({
	                  path: currentRoute.query.to_fullPath,
	                })
	              } else {
	                this.$router.push({
	                  path: "/Main",
	                  query: { username: this.userinfo.username }
	                })
	              }
                    }else {
                        this.$message.error('[' + res.status + ']' + res.message);
                    }

                     
                }
            ).catch(
                err => {
                    console.log("err:" + err);
                    //this.$message.error("登陆失败！");
                    this.$message.error('[' + err.status + ']' + err.message);
                }
            );
        }
    }
}
</script>    
<style>
    .login{
        padding:10%;
    }
</style>
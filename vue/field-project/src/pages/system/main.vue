<template>
  <div id="main">
    <el-container style="height: 960px; border: 1px solid #eee">
      <el-header>
        <div style="text-align:right;">
          <span>用户名：{{username}}</span>
          <el-button type="primary" @click="exit">exit</el-button>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200" style="background-color: rgb(238, 241, 246)">
          <el-menu :default-active="navselected" :active="navselected" @select="selectItems" class="el-menu-vertical-demo">
            <el-menu-item index="0">
              <i class="el-icon-document"></i>
              <!-- <router-link :to="{name: 'CodeGenerator'}" tag="span">代码生成器</router-link> -->
              <router-link :to="{name: 'CodeGenerator'}" tag="span">代码生成器</router-link>
            </el-menu-item>
            <el-menu-item index="1">
              <i class="el-icon-document"></i>
              <router-link :to="{name: 'FieldAdd'}" tag="span">field添加</router-link>
            </el-menu-item>
            <el-menu-item index="2">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'FieldList'}" tag="span">field列表</router-link>
            </el-menu-item>
            <el-menu-item index="3">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'CostInfo'}" tag="span">成本版本</router-link>
            </el-menu-item>
            <el-menu-item index="4">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'CostInfoList'}" tag="span">成本版本列表</router-link>
            </el-menu-item>

            <el-menu-item index="5">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'AirticketFareBasis'}" tag="span">机票基础</router-link>
            </el-menu-item>
            <el-menu-item index="6">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'AirticketFareBasisList'}" tag="span">机票基础列表</router-link>
            </el-menu-item>


            <el-menu-item index="9">
              <i class="el-icon-setting"></i>
              <router-link :to="{name: 'Test'}" tag="span">Test</router-link>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main height="500">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>

  </div>
</template>
<script>
  import defines from '../../config'

export default {
    name: 'Main',
    data() {
      return {
        username:"",
        navselected:"2",
        //defaultActive : "1"
      }
    },
    mounted : function() {
      this.username = sessionStorage.getItem('username');
    },
    methods: {
      getNavType(){
        this.navselected=this.$store.state.adminleftnavnum;
        //store.state.adminleftnavnum里值变化的时候，设置navselected
      },

      selectItems(index){
        this.$store.state.adminleftnavnum=index;
       //按钮选中之后设置当前的index为store里的值。
      },
      exit() { // 退出， 清理token,跳转到登陆页面
        sessionStorage.setItem('token','');
        this.$router.push({path: '/'});  
      }
    },
    watch: {
      // 监测store.state
      '$store.state.adminleftnavnum': 'getNavType'
    }
}

</script>


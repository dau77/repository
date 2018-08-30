<template>
    <div class="app">
        <el-form :inline="true" :model="form" class="demo-form-inline" style="text-align: left;">
          <el-form-item label="页面英文名称">
            <el-input v-model="form.pageEnname" placeholder="page_enname"></el-input>
          </el-form-item>
          <el-form-item label="语言">
            <el-select v-model="locale">
                <el-option v-for="language in languages" :key="language.key_value" :label="language.key_name" :value="language.key_value">
                </el-option>
            </el-select>
          </el-form-item>
                    
          <el-form-item>
            <el-button type="primary" @click="onSubmit(true)">查询</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="fieldData" width="100%" height="500" :default-sort = "{prop: 'field_id', order: 'descending'}">
            <el-table-column prop="field_name" sortable label="名称" width="100" align="center"></el-table-column>
            <el-table-column prop="field_enname" label="英文名" width="150" align="center"></el-table-column>
            <el-table-column prop="field_length" label="长度" width="50" align="center"></el-table-column>
            <el-table-column prop="field_type_name" label="类型" width="50" align="center"></el-table-column>
            <el-table-column prop="field_type_param" label="类型参数" width="100" align="center"></el-table-column>
            <el-table-column prop="field_order_by" label="排序" width="50" align="center"></el-table-column>
            <el-table-column prop="field_placeholder" label="占位符" width="100" align="center"></el-table-column>
            <el-table-column prop="field_rules" label="验证规则" width="500" align="center"></el-table-column>
            <el-table-column prop="table_name" label="对应表名" width="100" align="center"></el-table-column>
            <el-table-column prop="table_field" label="对应表字段" width="100" align="center"></el-table-column>
            <el-table-column prop="page_name" label="页面名称" width="100" align="center"></el-table-column>
            <el-table-column prop="page_enname" label="页面英文名" width="100" align="center"></el-table-column>
            <el-table-column prop="page_entity" label="页面实体" width="100" align="center"></el-table-column>
            <el-table-column label="操作" width="180"  align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="toEdit(scope.$index, scope.row.field_id)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deleteField(scope.$index, scope.row.field_id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="form.currentPage"
          :page-size="form.pageSize"
          layout="total, prev, pager, next, jumper"
          :total="form.total">
        </el-pagination>
    </div>
</template>
<script>
    import {get,deletes} from "../../api/base"
    import messages from "../../api/messages"
    import pageField from "../../api/field"
    export default {
        name:'FieldList',
        data() {
            return {
                form:{
                    pageEnname:'',
                    total:10,  // 数据总数
                    pageSize:10, // 每页显示数量
                    currentPage:1 // 当前所在页
                },
                fieldData:[
              
                ],
                languages:pageField.getLanguages(),
                locale:'zh_CN' // 语言:zh_CN, en_US，默认zh_CN中文，
            }
        },
        props:{

        },
        watch: {
          //在input子组件中不能直接改变在外部双向绑定的value
          value(value) {

          }
        },
        created () {
            // vuex 全局管理 调整菜单被选择的状态
            this.$store.state.adminleftnavnum="2";
        },
        mounted : function() {
            const url = 'field'
            const params = {size:this.form.pageSize}
            //var fieldInfo = get(url, params)
            get(url, params).then(res => {
                var info = res.response.data
                this.fieldData = info.field_list.records 
                this.form.total = info.field_list.total
                this.form.pageSize = info.field_list.size
                this.form.currentPage = info.field_list.current
            }).catch(err => {
                console.log("err:" + err);
                this.$message.error('[' + err.status + ']' + err.message);
            });
        },
        methods:{
            handleCurrentChange(val) {
                console.log('val:'+val);
                console.log('当前页: ${val}' + this.form.currentPage);
                this.form.currentPage = val
                this.onSubmit(false)
            },
            /**
             * isInitPages 是否初始化当前页码 true: 初始化为1，  false:不初始化，使用当前页码
             *  点查询时初始化当前页码， 如果不初始化，当查询出的结果集页码小于当前页码时，页码显示不正确
             */
            onSubmit(isInitPages) { 
                const url = 'field'
                const params = {
                    current: isInitPages ? 1 : this.form.currentPage, 
                    pageEnname:this.form.pageEnname,
                    size:this.form.pageSize,
                    locale:this.locale
                }
                    
                //var fieldInfo = get(url, params)
                get(url, params).then(res => {
                    var info = res.response.data
                    this.fieldData = info.field_list.records 
                    this.form.total = info.field_list.total
                    this.form.pageSize = info.field_list.size
                    this.form.currentPage = info.field_list.current
                    //this.form.pageSize = res.data.field_list.pages // 总页数
                }).catch(err => {
                    console.log("err:" + err);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            toEdit: function(index, field_id){ // $router.push 跳转到 name：FieldAdd的组件  query 里面是跳转携带的参数
                this.$router.push({name: 'FieldAdd',query: {fieldId: field_id, locale:this.locale}});
            },
            deleteField: function(index, field_id){ //
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const url = 'field/' + field_id
                    const params = {}
                    deletes(url, params).then(res => {
                        this.fieldData.splice(index,1);
                        this.form.total = this.form.total - 1
                        // $message  elementUI的消息提示框
                        this.$message.success(res.message);
                    }).catch(err => {
                        console.log("err:" + err);
                        this.$message.error('[' + err.status + ']' + err.message);
                    });
                }).catch(() => {
                    this.$message.info('已取消删除!');         
                });
            }
        }
    }
</script>
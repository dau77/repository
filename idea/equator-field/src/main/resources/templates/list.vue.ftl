<template>
    <div class="app">
        <el-table :data="dataList">
        <#list fields as field>
            <el-table-column prop="${field.fieldEnname}" label="${field.fieldName}" width="100" align="center"></el-table-column>
        </#list>
            <el-table-column label="操作" width="180"  align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="toEdit(scope.$index, scope.row.${idKeyFieldName})">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deleteField(scope.$index, scope.row.${idKeyFieldName})">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @current-change="handleCurrentChange"
                :current-page="pageForm.currentPage"
                :page-size="pageForm.pageSize"
                layout="total, prev, pager, next, jumper"
                :total="pageForm.total">
        </el-pagination>
    </div>
</template>
<script>
    import {get,deletes} from "../../api/base"
    import messages from "../../api/messages"
    import pageField from "../../api/field"
    export default {
        name: 'list',
        data(){
            return{
                pageForm:{
                    total:10,  // 数据总数
                    pageSize:10, // 每页显示数量
                    currentPage:1 // 当前所在页
                },
                dataList:[],
            }
        },
        created () {
            const url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}'
            const params = {size:this.pageForm.pageSize}
            get(url, params).then(res => {
                var info = res.response.data
                this.dataList = info.page.records
                this.pageForm.total = info.page.total
                this.pageForm.pageSize = info.page.size
                this.pageForm.currentPage = info.page.current
            }).catch(err => {
                    console.log("err:" + err);
                this.$message.error('[' + err.status + ']' + err.message);
            });
        },
        mounted : function() {},
        methods: {
            handleCurrentChange(val) {
                this.pageForm.currentPage = val
                this.onSubmit(false)
            },
            /**
             * isInitPages 是否初始化当前页码 true: 初始化为1，  false:不初始化，使用当前页码
             *  点查询时初始化当前页码， 如果不初始化，当查询出的结果集页码小于当前页码时，页码显示不正确
             */
            onSubmit(isInitPages) {
                const url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}'
                const params = {
                    current: isInitPages ? 1 : this.pageForm.currentPage,
                    size: this.pageForm.pageSize,
                }

                //var fieldInfo = get(url, params)
                get(url, params).then(res => {
                    var info = res.response.data
                    this.dataList = info.page.records
                    this.pageForm.total = info.page.total
                    this.pageForm.pageSize = info.page.size
                    this.pageForm.currentPage = info.page.current
                    //this.pageForm.pageSize = res.data.field_list.pages // 总页数
                }).catch(err => {
                    console.log("err:" + err);
                    this.$message.error('[' + err.status + ']' + err.message);
                });
            },
            toEdit: function (index, field_id) { // $router.push 跳转到 name：FieldAdd的组件  query 里面是跳转携带的参数
                this.$router.push({name: '${entity}', query: {fieldId: field_id}});
            },
            deleteField: function (index, field_id) { //
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const url = '<#if cfg.moduleName?? && cfg.moduleName != "">/${cfg.moduleName}</#if>/${table.name}/' + field_id
                    const params = {}
                    deletes(url, params).then(res => {
                        this.dataList.splice(index, 1);
                        this.pageForm.total = this.pageForm.total - 1
                        // $message  elementUI的消息提示框
                        this.$message.success(res.message);
                    }).
                        catch(err => {
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
<template>
    <div class="app">
        <el-table :data="dataList">
                    <el-table-column prop="cost_info_no" sortable label="成本版本号" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_table" sortable label="计划表" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_id" sortable label="计划id" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_no" sortable label="计划编号" width="100" align="center"></el-table-column>
            <el-table-column prop="version_remark" sortable label="成本版本说明" width="100" align="center"></el-table-column>
            <el-table-column prop="total_pay" sortable label="总应付金额折合人民币" width="100" align="center"></el-table-column>
            <el-table-column prop="total_paid" sortable label="总已付金额折合人民币" width="100" align="center"></el-table-column>
            <el-table-column prop="booked_num" sortable label="订购人数" width="100" align="center"></el-table-column>
            <el-table-column prop="version_name" sortable label="成本版本名称" width="100" align="center"></el-table-column>
            <el-table-column prop="who_add" sortable label="添加人" width="100" align="center"></el-table-column>
            <el-table-column prop="add_time" sortable label="添加时间" width="100" align="center"></el-table-column>
            <el-table-column prop="editor" sortable label="修改人" width="100" align="center"></el-table-column>
            <el-table-column prop="modify_time" sortable label="修改时间" width="100" align="center"></el-table-column>
            <el-table-column prop="check_time" sortable label="审核时间" width="100" align="center"></el-table-column>
            <el-table-column prop="is_last_version" sortable label="是否最后一个版本" width="100" align="center"></el-table-column>
            <el-table-column prop="cost_info_status" sortable label="成本版本状态" width="100" align="center"></el-table-column>
            <el-table-column prop="accounting_date" sortable label="会计日期" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_name" sortable label="计划名称" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_name_for_short" sortable label="计划简称" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_time_1" sortable label="出团日期" width="100" align="center"></el-table-column>
            <el-table-column prop="op_loc" sortable label="计划所属操作中心" width="100" align="center"></el-table-column>
            <el-table-column prop="plan_time_2" sortable label="回团日期" width="100" align="center"></el-table-column>
            <el-table-column prop="finally_flag" sortable label="计划是否已经终结" width="100" align="center"></el-table-column>
            <el-table-column prop="op" sortable label="计调" width="100" align="center"></el-table-column>
            <el-table-column label="操作" width="180"  align="center">
                <template slot-scope="scope">
                    <el-button size="mini" @click="toEdit(scope.$index, scope.row.cost_info_id)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deleteField(scope.$index, scope.row.cost_info_id)">删除</el-button>
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
            const url = 'cost_info'
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
                const url = 'cost_info'
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
                this.$router.push({name: 'CostInfo', query: {fieldId: field_id}});
            },
            deleteField: function (index, field_id) { //
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const url = 'cost_info/' + field_id
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
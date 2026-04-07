<template>
    <div class="fater-body-show">
        <el-card shadow="never">
            <div
                class="el-card-header"
                slot="header"
                style="font-size: 26px"
            >
            <i class="iconfont icon-r-find" style="font-size: 26px"></i>
                信息查询
            </div>
            <div>
                <el-form :inline="true" :model="qryForm">
                    <el-form-item>
                        <el-input
                            v-model="qryForm.clubId"
                            placeholder="输入社团ID…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.userId"
                            placeholder="输入用户ID…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="qryForm.status" placeholder="请选择状态">
                            <el-option label="查看全部" value=""></el-option>
                            <el-option label="未缴费" :value="0"></el-option>
                            <el-option label="已缴费" :value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            type="primary"
                            style="font-size: 18px"
                            @click="getPageLikeInfo()"
                        >
                            搜索</el-button
                        >
                    </el-form-item>
                </el-form>
            </div>
        </el-card>

        <el-card shadow="never">
            <div v-if="userType == 1" slot="header">
                <el-button
                    type="primary"
                    style="font-size: 18px"
                    @click="showAddWin()"
                    >
                    新增</el-button
                >
            </div>
            <div>
                <el-table
                    v-loading="loading"
                    element-loading-text="拼命加载中"
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(124, 124, 124, 0.8)"
                    :data="pageInfos"
                    border
                >
                    <el-table-column
                        align="center"
                        type="index"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="noticeTitle"
                        label="缴费通知"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="userName"
                        label="用户账号"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="realName"
                        label="成员姓名"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="amount"
                        label="缴纳金额（元）"
                    ></el-table-column>
                    <el-table-column align="center" label="状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.status == 1" style="color: green">已缴费</span>
                            <span v-if="scope.row.status == 0" style="color: red">未缴费</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="缴费时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.payTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        v-if="userType == 2"
                        align="center"
                        label="操作处理"
                        fixed="right"
						width="140"
                    >
                        <template slot-scope="scope">
                            <el-button
                                v-if="scope.row.status == 0"
                                type="success" style="font-size: 18px"
                                @click="pay(scope.row.id)"
                                > 去缴费</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                    v-if="pageTotal >= 0"
                    style="margin-top: 15px"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageIndex"
                    :page-sizes="[5, 10, 20, 50]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalInfo"
                >
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="添加信息" width="600px" :visible.sync="showAddFlag">
            <el-form label-width="90px" :model="payLogsForm">
                <el-form-item label="缴纳费用（元）">
                    <el-input
                        v-model="payLogsForm.total"
                        onkeyup="value=value.replace(/[^\d.]/g,'')"
                        placeholder="请输入缴纳费用…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="收费社团">
                    <el-select
                        style="width: 100%"
                        v-model="payLogsForm.teamId"
                        placeholder="请输入收费社团…"
                    >
                        <el-option
                            v-for="(item, index) in teams"
                            :key="index"
                            :label="item.name"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费用户">
                    <el-input
                        v-model="payLogsForm.userId"
                        placeholder="请输入缴费用户ID…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showAddFlag = false" style="font-size: 18px"
                    >
                    取 消</el-button
                >
                <el-button
                    type="primary"
                    @click="addInfo()"
                    style="font-size: 18px"
                    >
                    确 定</el-button
                >
            </div>
        </el-dialog>

        <el-dialog title="修改信息" width="600px" :visible.sync="showUpdFlag">
            <el-form label-width="90px" :model="payLogsForm">
                <el-form-item label="缴纳费用">
                    <el-input
                        v-model="payLogsForm.total"
                        type="number"
                        onkeyup="value=value.replace(/[^\d.]/g,'')"
                        placeholder="请输入缴纳费用…"
                        autocomplete="off"
                    ></el-input>
                    元
                </el-form-item>
                <el-form-item label="收费社团">
                    <el-select
                        style="width: 100%"
                        v-model="payLogsForm.teamId"
                        placeholder="请输入收费社团…"
                    >
                        <el-option
                            v-for="(item, index) in teams"
                            :key="index"
                            :label="item.name"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费用户">
                    <el-input
                        v-model="payLogsForm.userId"
                        placeholder="请输入缴费用户ID…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showUpdFlag = false" style="font-size: 18px"
                    >
                    取 消</el-button
                >
                <el-button
                    type="primary"
                    @click="updInfo()"
                    style="font-size: 18px"
                    >
                    确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<style>
</style>

<script>
import {
    getLoginUser,
    getPagePayLogs,
    addPayLogs,
    payRecord,
    delPayLogs,
} from "../../api";


export default {
    data() {
        return {
            userType: "",
            pageInfos: [],
            pageIndex: 1,
            pageSize: 10,
            pageTotal: 0,
            totalInfo: 0,
            loading: true,
            showAddFlag: false,
            showUpdFlag: false,
            qryForm: {
                clubId: "",
                userId: "",
                status: ""
            },
            payLogsForm: {
                id: "",
                amount: "",
                noticeId: "",
                userId: "",
            },
        };
    },
    methods: {
        formatDate(time) {
            if (!time) return '';
            const date = new Date(time);
            const y = date.getFullYear();
            const m = (date.getMonth() + 1).toString().padStart(2, '0');
            const d = date.getDate().toString().padStart(2, '0');
            const h = date.getHours().toString().padStart(2, '0');
            const min = date.getMinutes().toString().padStart(2, '0');
            const s = date.getSeconds().toString().padStart(2, '0');
            return `${y}-${m}-${d} ${h}:${min}:${s}`;
        },
        getPageInfo(pageIndex, pageSize) {
            let uId = this.userType == 2 ? this.$store.state.userInfo.id : this.qryForm.userId;
            getPagePayLogs(pageIndex, pageSize, this.qryForm.clubId, uId, this.qryForm.status).then(
                (resp) => {
                    this.pageInfos = resp.data.data;
                    this.pageIndex = resp.data.pageIndex;
                    this.pageSize = resp.data.pageSize;
                    this.pageTotal = resp.data.pageTotal;
                    this.totalInfo = resp.data.count;

                    this.loading = false;
                }
            );
        },
        getPageLikeInfo() {
            let uId = this.userType == 2 ? this.$store.state.userInfo.id : this.qryForm.userId;
            getPagePayLogs(
                1,
                this.pageSize,
                this.qryForm.clubId,
                uId,
                this.qryForm.status
            ).then((resp) => {
                this.pageInfos = resp.data.data;
                this.pageIndex = resp.data.pageIndex;
                this.pageSize = resp.data.pageSize;
                this.totalInfo = resp.data.count;
                this.pageTotal = resp.data.pageTotal;
                this.loading = false;
            });
        },
        handleSizeChange(pageSize) {
            this.getPageInfo(
                this.pageIndex,
                pageSize
            );
        },
        handleCurrentChange(pageIndex) {
            this.getPageInfo(
                pageIndex,
                this.pageSize
            );
        },
        initForm() {
            this.payLogsForm = {
                id: "",
                amount: "",
                noticeId: "",
                userId: "",
            };
        },
        showAddWin() {
            this.initForm();
            this.showAddFlag = true;
        },
        pay(id) {
            payRecord(id).then(resp => {
                if(resp.code == 0) {
                    this.$message.success("缴费成功");
                    this.getPageInfo(1, this.pageSize);
                } else {
                    this.$message.warning(resp.msg);
                }
            })
        },
        addInfo() {
            addPayLogs(this.payLogsForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);

                this.showAddFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("即将删除相关信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                delPayLogs(id).then((resp) => {
                    this.$message({
                        message: resp.msg,
                        type: "success",
                    });

                    this.getPageInfo(1, this.pageSize);
                });
            });
        },
    },
    mounted() {
        getLoginUser(this.$store.state.token).then((resp) => {
            this.userType = resp.data.type;
            this.$store.state.userInfo = resp.data;
            this.getPageInfo(1, this.pageSize);
        });
    },
};
</script>
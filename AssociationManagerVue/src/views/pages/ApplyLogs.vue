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
                        <el-button type="primary" @click="getPageLikeInfo()" style="font-size: 18px">
                             搜索</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>

        <el-card shadow="never">
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
                        prop="clubName"
                        label="社团名称"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="userName"
                        label="申请人账号"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="realName"
                        label="申请人姓名"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="joinReason"
                        label="入社原因"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        label="申请时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.applyTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="审批状态"
                        width="120"
                    >
                        <template slot-scope="scope">
                            <el-tag
                                effect="plain"
                                type="success"
                                v-if="scope.row.auditStatus == '1'"
                                >已通过</el-tag
                            >
                            <el-tag
                                effect="plain"
                                type="danger"
                                v-if="scope.row.auditStatus == '2'"
                                >已驳回</el-tag
                            >
                            <el-tag
                                effect="plain"
                                type="info"
                                v-if="scope.row.auditStatus == '0'"
                                >审核中</el-tag
                            >
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="审批时间"
                        width="160"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.auditTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        v-if="userType == 1 || userType == 0"
                        align="center"
                        label="操作处理"
                        fixed="right"
                        width="240"
                    >
                        <template slot-scope="scope">
                            <el-button
                                v-if="scope.row.auditStatus == '0'"
                                type="primary"
                                @click="updInfo(scope.row, '1')"
                            >
                                通过</el-button
                            >
                            <el-button
                                v-if="scope.row.auditStatus == '0'"
                                type="danger"
                                @click="updInfo(scope.row, '2')"
                            >
                                驳回</el-button
                            >
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
    </div>
</template>

<style>
</style>

<script>
import { getLoginUser, getPageJoinClubApplications, auditJoinClubApplication } from "../../api";

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
            qryForm: {
                clubId: "",
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
            const userId = this.userType == 2 && this.$store.state.userInfo ? this.$store.state.userInfo.id : null;
            getPageJoinClubApplications(pageIndex, pageSize, this.qryForm.clubId, userId).then((resp) => {
                const total = resp.data.total || 0;
                this.pageInfos = resp.data.data || [];
                this.pageIndex = pageIndex;
                this.pageSize = pageSize;
                this.totalInfo = total;
                this.pageTotal = Math.ceil(total / pageSize);
                this.loading = false;
            });
        },
        getPageLikeInfo() {
            this.getPageInfo(1, this.pageSize);
        },
        handleSizeChange(pageSize) {
            this.getPageInfo(
                this.pageIndex,
                pageSize,
            );
        },
        handleCurrentChange(pageIndex) {
            this.getPageInfo(
                pageIndex,
                this.pageSize,
            );
        },
        updInfo(data, status) {
            auditJoinClubApplication(data.joinAppId, status).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);
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

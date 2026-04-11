<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item v-if="true">
                        <el-input v-model="qryForm.userId" placeholder="输入申请人ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item v-if="true">
                        <el-input v-model="qryForm.activityId" placeholder="输入活动ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="qryForm.auditStatus" placeholder="审核状态">
                            <el-option label="全部" value=""></el-option>
                            <el-option label="待审核" :value="0"></el-option>
                            <el-option label="已通过" :value="1"></el-option>
                            <el-option label="已拒绝" :value="2"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="warning" icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div>
                <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)" :data="pageInfos" border>
                    <el-table-column align="center" type="index" label="序号"></el-table-column>
                    <el-table-column align="center" prop="activityTitle" label="活动标题"></el-table-column>
                    <el-table-column align="center" prop="userName" label="申请人账号"></el-table-column>
                    <el-table-column align="center" prop="realName" label="申请人姓名"></el-table-column>
                    <el-table-column align="center" label="报名时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.applyTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="审核状态">
                        <template slot-scope="scope">
                            <el-tag type="warning" v-if="scope.row.auditStatus == 0">待审核</el-tag>
                            <el-tag type="success" v-if="scope.row.auditStatus == 1">已通过</el-tag>
                            <el-tag type="danger" v-if="scope.row.auditStatus == 2">已拒绝</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" prop="applyReason" label="申请理由"></el-table-column>
                    <el-table-column align="center" prop="feedback" label="审批反馈"></el-table-column>
                    <el-table-column align="center" label="审批时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.auditTime) }}
                        </template>
                    </el-table-column>
                    
                    <el-table-column align="center" label="操作处理" width="160" fixed="right">
                        <template slot-scope="scope">
                            <!-- 社长操作 -->
                            <el-button v-if="userType == 1 && scope.row.auditStatus == 0" type="success" size="mini" @click="audit(scope.row.id, 1)">通过</el-button>
                            <el-button v-if="userType == 1 && scope.row.auditStatus == 0" type="danger" size="mini" @click="audit(scope.row.id, 2)">拒绝</el-button>
                            
                            <!-- 用户操作 -->
                            <el-button v-if="userType == 2 && scope.row.auditStatus == 1 && scope.row.activityStatus == 1" type="primary" size="mini" @click="signIn(scope.row)">签到</el-button>
                            <span v-if="userType == 2 && scope.row.auditStatus == 1 && scope.row.activityStatus != 1" style="color: gray">等待发起签到</span>
                            <el-button v-if="userType == 2 && scope.row.auditStatus == 1 && scope.row.activityStatus == 2" type="warning" size="mini" @click="signOut(scope.row)">签退</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[10, 20, 50]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
                </el-pagination>
            </div>
        </el-card>
    </div>
</template>

<script>
import { getLoginUser, getPageActivityParticipants, auditActivityParticipantWithFeedback, punchCardSignIn, punchCardSignOut } from "../../api";

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
                activityId: "",
                userId: "",
                auditStatus: "",
            }
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
            getPageActivityParticipants(pageIndex, pageSize, this.qryForm.activityId, uId, this.qryForm.auditStatus).then((resp) => {
                this.pageInfos = resp.data.data;
                this.pageIndex = resp.data.pageIndex;
                this.pageSize = resp.data.pageSize;
                this.pageTotal = resp.data.pageTotal;
                this.totalInfo = resp.data.count;
                this.loading = false;
            });
        },
        getPageLikeInfo() {
            this.getPageInfo(1, this.pageSize);
        },
        handleSizeChange(pageSize) {
            this.getPageInfo(this.pageIndex, pageSize);
        },
        handleCurrentChange(pageIndex) {
            this.getPageInfo(pageIndex, this.pageSize);
        },
        audit(id, status) {
            this.$prompt("请输入审批反馈(可选)", "审批", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                inputPlaceholder: "例如：欢迎加入 / 名额已满…",
            }).then(({ value }) => {
                auditActivityParticipantWithFeedback(id, status, value).then(resp => {
                    this.$message.success(resp.msg);
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
            }).catch(() => {});
        },
        signIn(row) {
            this.$prompt("请输入签到码(signInId)", "签到打卡", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                inputPlaceholder: "社长发起签到后生成的ID",
            }).then(({ value }) => {
                punchCardSignIn(value, row.userId).then(resp => {
                    this.$message.success(resp.msg);
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
            }).catch(() => {});
        },
        signOut(row) {
            this.$prompt("请输入签退码(signOutId)", "签退打卡", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                inputPlaceholder: "社长发起签退后生成的ID",
            }).then(({ value }) => {
                punchCardSignOut(value, row.userId).then(resp => {
                    this.$message.success(resp.msg);
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
            }).catch(() => {});
        }
    },
    mounted() {
        getLoginUser(this.$store.state.token).then((resp) => {
            this.userType = resp.data.type;
            this.$store.state.userInfo = resp.data;
            this.getPageInfo(1, this.pageSize);
        });
    }
};
</script>

<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item v-if="userType == 1 || userType == 0">
                        <el-input v-model="qryForm.userId" placeholder="输入申请人ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item v-if="userType == 2">
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
                    <el-table-column align="center" label="签到时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.signInTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="签退时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.signOutTime) }}
                        </template>
                    </el-table-column>
                    
                    <el-table-column align="center" label="操作处理" width="160" fixed="right">
                        <template slot-scope="scope">
                            <!-- 社长操作 -->
                            <el-button v-if="userType == 1 && scope.row.auditStatus == 0" type="success" size="mini" @click="audit(scope.row.id, 1)">通过</el-button>
                            <el-button v-if="userType == 1 && scope.row.auditStatus == 0" type="danger" size="mini" @click="audit(scope.row.id, 2)">拒绝</el-button>
                            
                            <!-- 用户操作 -->
                            <el-button v-if="userType == 2 && scope.row.auditStatus == 1 && !scope.row.signInTime && scope.row.activityStatus == 1" type="primary" size="mini" @click="signIn(scope.row.id)">签到</el-button>
                            <span v-if="userType == 2 && scope.row.auditStatus == 1 && !scope.row.signInTime && scope.row.activityStatus == 0" style="color: gray">等待发起签到</span>
                            <span v-if="userType == 2 && scope.row.auditStatus == 1 && !scope.row.signInTime && scope.row.activityStatus > 1" style="color: red">缺席签到</span>
                            
                            <el-button v-if="userType == 2 && scope.row.signInTime && !scope.row.signOutTime && scope.row.activityStatus == 2" type="warning" size="mini" @click="signOut(scope.row.id)">签退</el-button>
                            <span v-if="userType == 2 && scope.row.signInTime && !scope.row.signOutTime && scope.row.activityStatus < 2" style="color: gray">等待发起签退</span>
                            <span v-if="userType == 2 && scope.row.signInTime && !scope.row.signOutTime && scope.row.activityStatus > 2" style="color: red">缺席签退</span>
                            
                            <span v-if="userType == 2 && scope.row.signOutTime" style="color: green">已完成</span>
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
import { getLoginUser, getPageActivityParticipants, auditActivityParticipant, signInActivity, signOutActivity } from "../../api";

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
            auditActivityParticipant(id, status).then(resp => {
                this.$message.success(resp.msg);
                this.getPageInfo(this.pageIndex, this.pageSize);
            });
        },
        signIn(id) {
            signInActivity(id).then(resp => {
                this.$message.success(resp.msg);
                this.getPageInfo(this.pageIndex, this.pageSize);
            });
        },
        signOut(id) {
            signOutActivity(id).then(resp => {
                this.$message.success(resp.msg);
                this.getPageInfo(this.pageIndex, this.pageSize);
            });
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
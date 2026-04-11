<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item>
                        <el-input v-model="qryForm.activityId" placeholder="输入活动ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="qryForm.userId" placeholder="输入用户ID…" autocomplete="off"></el-input>
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
                    <el-table-column align="center" prop="userName" label="用户账号"></el-table-column>
                    <el-table-column align="center" prop="realName" label="成员姓名"></el-table-column>
                    <el-table-column align="center" label="签到状态">
                        <template slot-scope="scope">
                            <el-tag type="success" v-if="scope.row.status == 1">已签到</el-tag>
                            <el-tag type="danger" v-if="scope.row.status == 0">未签到</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="签到时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.signTime) }}
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
import { getLoginUser, getPageSignInRecords } from "../../api";

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
            getPageSignInRecords(pageIndex, pageSize, this.qryForm.activityId, uId).then((resp) => {
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
<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item>
                        <el-input v-model="qryForm.title" placeholder="输入推文标题…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="qryForm.activityId" placeholder="输入活动ID…" autocomplete="off"></el-input>
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
                    <el-table-column align="center" prop="title" label="推文标题"></el-table-column>
                    <el-table-column align="center" prop="activityTitle" label="活动标题"></el-table-column>
                    <el-table-column align="center" prop="publisherName" label="发布人"></el-table-column>
                    <el-table-column align="center" prop="content" label="推文内容" show-overflow-tooltip></el-table-column>
                    <el-table-column align="center" label="发布时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column v-if="userType == 1 || userType == 0" align="center" label="操作处理" width="120" fixed="right">
                        <template slot-scope="scope">
                            <div style="display: flex; justify-content: center; align-items: center; gap: 8px;">
                                <el-button type="danger" size="mini" @click="delInfo(scope.row.id)">删除</el-button>
                            </div>
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
import { getLoginUser, getPageActivityTweets, delActivityTweet } from "../../api";

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
                title: "",
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
            getPageActivityTweets(pageIndex, pageSize, this.qryForm.activityId, this.qryForm.title).then((resp) => {
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
        delInfo(id) {
            this.$confirm("确定要删除这条推文吗?", "提示", { type: "warning" }).then(() => {
                delActivityTweet(id).then(resp => {
                    this.$message.success(resp.msg);
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
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
<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item v-if="userType == 1 || userType == 0">
                        <el-input v-model="qryForm.userId" placeholder="输入提交人ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="qryForm.activityId" placeholder="输入活动ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="warning" icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
                        <el-button v-if="userType == 2" type="primary" icon="el-icon-plus" @click="showAddWin()"></el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div>
                <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)" :data="pageInfos" border>
                    <el-table-column align="center" type="index" label="序号"></el-table-column>
                    <el-table-column align="center" prop="activityTitle" label="活动标题"></el-table-column>
                    <el-table-column align="center" prop="userName" label="提交人账号"></el-table-column>
                    <el-table-column align="center" prop="realName" label="提交人姓名"></el-table-column>
                    <el-table-column align="center" prop="content" label="心得内容" show-overflow-tooltip></el-table-column>
                    <el-table-column align="center" label="提交时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    
                    <el-table-column v-if="userType == 2" align="center" label="操作处理" width="120" fixed="right">
                        <template slot-scope="scope">
                            <el-button type="danger" size="mini" @click="delInfo(scope.row.id)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[10, 20, 50]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="提交心得体会" width="600px" :visible.sync="showAddFlag">
            <el-form label-width="90px" :model="fbForm">
                <el-form-item label="活动ID">
                    <el-input v-model="fbForm.activityId" placeholder="请输入已结束或已参与的活动ID…" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="心得内容">
                    <el-input v-model="fbForm.content" type="textarea" rows="6" placeholder="请输入心得内容…" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showAddFlag = false" style="font-size: 18px"> 取 消</el-button>
                <el-button type="primary" @click="addInfo()" style="font-size: 18px"> 提 交</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { getLoginUser, getPageActivityFeedbacks, addActivityFeedback, delActivityFeedback } from "../../api";

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
            qryForm: {
                activityId: "",
                userId: "",
            },
            fbForm: {
                activityId: "",
                userId: "",
                content: ""
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
            getPageActivityFeedbacks(pageIndex, pageSize, this.qryForm.activityId, uId).then((resp) => {
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
        showAddWin() {
            this.fbForm = {
                activityId: "",
                userId: this.$store.state.userInfo.id,
                content: ""
            };
            this.showAddFlag = true;
        },
        addInfo() {
            addActivityFeedback(this.fbForm).then((resp) => {
                this.$message.success(resp.msg);
                this.getPageInfo(1, this.pageSize);
                this.showAddFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("确定要删除这条心得记录吗?", "提示", { type: "warning" }).then(() => {
                delActivityFeedback(id).then(resp => {
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
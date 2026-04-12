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
                    <el-form-item v-if="userType == 1">
                        <el-button type="primary" @click="showAddWin()">新增推文</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div>
                <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)" :data="pageInfos" border>
                    <el-table-column align="center" type="index" label="序号"></el-table-column>
                    <el-table-column align="center" label="推文标题">
                        <template slot-scope="scope">
                            <el-link type="primary" @click="showDetail(scope.row)">{{ scope.row.title }}</el-link>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" prop="activityTitle" label="活动标题"></el-table-column>
                    <el-table-column align="center" prop="publisherName" label="发布人"></el-table-column>
                    <el-table-column align="center" prop="content" label="推文内容" show-overflow-tooltip></el-table-column>
                    <el-table-column align="center" label="发布时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column v-if="userType == 1 || userType == 0" align="center" label="操作处理" width="160" fixed="right">
                        <template slot-scope="scope">
                            <div style="display: flex; justify-content: center; align-items: center; gap: 8px;">
                                <el-button v-if="userType == 1" type="primary" size="mini" @click="showUpdWin(scope.row)">编辑</el-button>
                                <el-button type="danger" size="mini" @click="delInfo(scope.row.id)">删除</el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination v-if="pageTotal >= 0" style="margin-top: 15px;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[10, 20, 50]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="推文详情" width="600px" :visible.sync="showDetailFlag">
            <el-descriptions :column="1" border>
                <el-descriptions-item label="推文标题">{{ currentTweet.title }}</el-descriptions-item>
                <el-descriptions-item label="活动标题">{{ currentTweet.activityTitle }}</el-descriptions-item>
                <el-descriptions-item label="发布人">{{ currentTweet.publisherName }}</el-descriptions-item>
                <el-descriptions-item label="发布时间">{{ formatDate(currentTweet.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="推文内容">
                    <div style="white-space: pre-wrap;">{{ currentTweet.content }}</div>
                </el-descriptions-item>
            </el-descriptions>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="showDetailFlag = false">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog :title="isAdd ? '新增推文' : '编辑推文'" width="600px" :visible.sync="showFormFlag">
            <el-form label-width="90px">
                <el-form-item label="推文标题">
                    <el-input v-model="tweetForm.title" placeholder="请输入推文标题"></el-input>
                </el-form-item>
                <el-form-item label="活动ID">
                    <el-input v-model="tweetForm.activityId" placeholder="请输入活动ID"></el-input>
                </el-form-item>
                <el-form-item label="推文内容">
                    <el-input type="textarea" :rows="6" v-model="tweetForm.content" placeholder="请输入推文内容"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showFormFlag = false">取消</el-button>
                <el-button type="primary" @click="submitForm()">保存</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { getLoginUser, getPageActivityTweets, addActivityTweet, updActivityTweet, delActivityTweet } from "../../api";

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
            showDetailFlag: false,
            showFormFlag: false,
            isAdd: true,
            currentTweet: {},
            tweetForm: {
                id: "",
                title: "",
                content: "",
                activityId: ""
            },
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
        showDetail(row) {
            this.currentTweet = row;
            this.showDetailFlag = true;
        },
        showAddWin() {
            this.isAdd = true;
            this.tweetForm = { id: "", title: "", content: "", activityId: "" };
            this.showFormFlag = true;
        },
        showUpdWin(row) {
            this.isAdd = false;
            this.tweetForm = { ...row };
            this.showFormFlag = true;
        },
        submitForm() {
            if (!this.tweetForm.title || !this.tweetForm.content || !this.tweetForm.activityId) {
                this.$message.warning("请完整填写表单");
                return;
            }
            if (this.isAdd) {
                addActivityTweet(this.tweetForm).then(resp => {
                    this.$message.success("新增成功");
                    this.showFormFlag = false;
                    this.getPageInfo(1, this.pageSize);
                });
            } else {
                updActivityTweet(this.tweetForm).then(resp => {
                    this.$message.success("编辑成功");
                    this.showFormFlag = false;
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
            }
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
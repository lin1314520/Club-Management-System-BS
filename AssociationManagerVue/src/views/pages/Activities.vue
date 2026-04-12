
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
                            placeholder="输入社团ID"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.title"
                            placeholder="输入活动标题…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            type="primary"
                            @click="getPageLikeInfo()"
                            style="font-size: 18px"
                        >
                            搜索</el-button
                        >
                    </el-form-item>
                </el-form>
            </div>
        </el-card>

        <el-card shadow="never">
            <div v-if="userType == 0 || userType == 1" slot="header">
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
                        prop="title"
                        label="活动标题"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="clubName"
                        label="发布社团"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        label="开始时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.startTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="结束时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.endTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="location"
                        label="活动地点"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        label="参与人员"
                        width="170"
                    >
                        <template slot-scope="scope">
                            <el-popover
                                title="活动参与人员"
                                trigger="click"
                                @show="getActivePeople(scope.row.id)"
                                :width="550"
                                placement="left"
                            >
                                <el-table :data="activeLogs" border>
                                    <el-table-column
                                        align="center"
                                        type="index"
                                    ></el-table-column>
                                    <el-table-column
                                        align="center"
                                        prop="userName"
                                        label="参与人员账号"
                                    ></el-table-column>
                                    <el-table-column
                                        align="center"
                                        prop="realName"
                                        label="姓名"
                                    ></el-table-column>
                                    <el-table-column
                                        align="center"
                                        label="报名时间"
                                    >
                                        <template slot-scope="scope">
                                            {{ formatDate(scope.row.applyTime) }}
                                        </template>
                                    </el-table-column>
                                </el-table>
                                <el-button
                                    type="primary"
                                    style="font-size: 18px"
                                    slot="reference"
                                    >
                                    参与人员</el-button
                                >
                            </el-popover>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="活动状态" width="100">
                        <template slot-scope="scope">
                            <el-tag type="info" v-if="scope.row.status == 0">未开始</el-tag>
                            <el-tag type="warning" v-if="scope.row.status == 1">签到中</el-tag>
                            <el-tag type="warning" v-if="scope.row.status == 2">签退中</el-tag>
                            <el-tag type="success" v-if="scope.row.status == 3">已结束</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="操作处理"
                        fixed="right"
                        :width="userType == 2 ? 140 : 320"
                    >
                        <template slot-scope="scope">
                            <div style="display: flex; justify-content: center; align-items: center; gap: 8px;">
                                <template v-if="userType == 0 || userType == 1">
                                    <el-button v-if="scope.row.status == 0" type="primary" size="mini" @click="changeStatus(scope.row, 1)">发起签到</el-button>
                                    <el-button v-if="scope.row.status == 1" type="warning" size="mini" @click="changeStatus(scope.row, 2)">发起签退</el-button>
                                    <el-button v-if="scope.row.status == 2" type="danger" size="mini" @click="changeStatus(scope.row, 3)">结束活动</el-button>
                                    <el-button v-if="scope.row.status == 3" type="success" size="mini" @click="showTweetWin(scope.row)">发布推文</el-button>
                                    <el-button
                                        style="font-size: 14px"
                                        type="danger"
                                        size="mini"
                                        @click="delInfo(scope.row.id)"
                                        >
                                        删除</el-button
                                    >
                                </template>
                                <template v-else-if="userType == 2">
                                    <el-button
                                        v-if="scope.row.myAuditStatus === -1 || scope.row.myAuditStatus === undefined"
                                        @click="active(scope.row.id)"
                                        size="mini"
                                        type="primary"
                                        >
                                        我要报名</el-button
                                    >
                                    <el-button
                                        v-else-if="scope.row.myAuditStatus === 0"
                                        disabled
                                        size="mini"
                                        type="info"
                                        >
                                        等待审核中</el-button
                                    >
                                    <el-button
                                        v-else-if="scope.row.myAuditStatus === 1"
                                        disabled
                                        size="mini"
                                        type="success"
                                        >
                                        已通过审核</el-button
                                    >
                                    <el-button
                                        v-else-if="scope.row.myAuditStatus === 2"
                                        disabled
                                        size="mini"
                                        type="danger"
                                        >
                                        报名已拒绝</el-button
                                    >
                                </template>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="了解更多"
                        width="170"
                    >
                        <template slot-scope="scope">
                            <el-popover
                                trigger="click"
                                :width="700"
                                placement="left"
                            >
                                <el-descriptions
                                    :column="1"
                                    size="small"
                                    border
                                >
                                    <el-descriptions-item>
                                        <template slot="label"
                                            >活动标题</template
                                        >
                                        {{ scope.row.title }}
                                    </el-descriptions-item>
                                    <el-descriptions-item>
                                        <template slot="label"
                                            >发布社团</template
                                        >
                                        {{ scope.row.clubName }}
                                    </el-descriptions-item>
                                    <el-descriptions-item>
                                        <template slot="label"
                                            >活动时间</template
                                        >
                                        {{ formatDate(scope.row.startTime) }} 至 {{ formatDate(scope.row.endTime) }}
                                    </el-descriptions-item>
                                    <el-descriptions-item>
                                        <template slot="label"
                                            >活动地点</template
                                        >
                                        {{ scope.row.location }}
                                    </el-descriptions-item>
                                    <el-descriptions-item>
                                        <template slot="label"
                                            >活动详情</template
                                        >
                                        {{ scope.row.content }}
                                    </el-descriptions-item>
                                </el-descriptions>
                                <el-button
                                    type="primary"
                                    style="font-size: 18px"
                                    slot="reference"
                                    >
                                    查看详情</el-button
                                >
                            </el-popover>
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

        <el-dialog
            title="添加信息"
            :fullscreen="true"
            :visible.sync="showAddFlag"
        >
            <el-form label-width="90px" :model="activitiesForm">
                <el-form-item label="活动标题">
                    <el-input
                        v-model="activitiesForm.title"
                        placeholder="请输入活动标题…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="发布社团">
                    <el-select
                        style="width: 100%"
                        v-model="activitiesForm.clubId"
                        placeholder="请选择发布社团…"
                    >
                        <el-option
                            v-for="(item, index) in teams"
                            :key="index"
                            :label="item.clubName"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="活动地点">
                    <el-input
                        v-model="activitiesForm.location"
                        placeholder="请输入活动地点…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="开始时间">
                    <el-date-picker
                        style="width: 100%"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        v-model="activitiesForm.startTime"
                        type="datetime"
                        placeholder="选择开始时间…"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间">
                    <el-date-picker
                        style="width: 100%"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        v-model="activitiesForm.endTime"
                        type="datetime"
                        placeholder="选择结束时间…"
                    ></el-date-picker>
                </el-form-item>
                <el-form-item label="活动详情">
                    <el-input
                        type="textarea"
                        rows="10"
                        v-model="activitiesForm.content"
                        placeholder="请输入活动详情…"
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
        <el-dialog title="发布活动推文" width="600px" :visible.sync="showTweetFlag">
            <el-form label-width="90px">
                <el-form-item label="推文标题">
                    <el-input v-model="tweetForm.title" placeholder="请输入推文标题"></el-input>
                </el-form-item>
                <el-form-item label="推文内容">
                    <el-input type="textarea" :rows="5" v-model="tweetForm.content" placeholder="请输入活动总结与推文内容"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showTweetFlag = false" style="font-size: 18px">取消</el-button>
                <el-button type="primary" @click="submitTweet()" style="font-size: 18px">发布</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<style>
</style>

<script>
import {
    getLoginUser,
    getActiveLogs,
    getPageActivities,
    addActivities,
    delActivities,
    updActivities,
    getAllTeamList,
    applyJoinActivity,
    getPageActivityParticipants,
    addNotices,
    launchActivitySignIn,
    launchActivitySignOut
} from "../../api";

export default {
    data() {
        return {
            teams: [],
            userType: "",
            activeLogs: [],
            pageInfos: [],
            pageIndex: 1,
            pageSize: 10,
            pageTotal: 0,
            totalInfo: 0,
            loading: true,
            showAddFlag: false,
            showTweetFlag: false,
            qryForm: {
                clubId: "",
                title: "",
            },
            activitiesForm: {
                id: "",
                clubId: "",
                title: "",
                content: "",
                location: "",
                startTime: "",
                endTime: "",
            },
            tweetForm: {
                title: "",
                content: "",
                clubId: "",
                publisherId: ""
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
        getActivePeople(activeId) {
            getActiveLogs(activeId).then((resp) => {
                this.activeLogs = [];
                this.activeLogs = resp.data;
            });
        },

        changeStatus(row, newStatus) {
            const statusMap = { 1: "签到", 2: "签退", 3: "结束" };
            this.$confirm(`确认要对活动【${row.title}】发起${statusMap[newStatus]}吗?`, "提示", {
                type: "warning"
            }).then(() => {
                if (newStatus == 1) {
                    launchActivitySignIn(row.id).then((r) => {
                        if (r.code != 0) {
                            this.$message.warning(r.msg);
                            return;
                        }
                        updActivities({ id: row.id, status: newStatus }).then(() => {
                            this.$alert(`签到已发起，签到码：${r.data}`, "签到码", { confirmButtonText: "确定" });
                            this.getPageInfo(this.pageIndex, this.pageSize);
                        });
                    });
                    return;
                }
                if (newStatus == 2) {
                    launchActivitySignOut(row.id).then((r) => {
                        if (r.code != 0) {
                            this.$message.warning(r.msg);
                            return;
                        }
                        updActivities({ id: row.id, status: newStatus }).then(() => {
                            this.$alert(`签退已发起，签退码：${r.data}`, "签退码", { confirmButtonText: "确定" });
                            this.getPageInfo(this.pageIndex, this.pageSize);
                        });
                    });
                    return;
                }
                updActivities({ id: row.id, status: newStatus }).then(() => {
                    this.$message.success("操作成功");
                    this.getPageInfo(this.pageIndex, this.pageSize);
                });
            });
        },
        showTweetWin(row) {
            this.tweetForm = {
                title: `【活动总结】${row.title}`,
                content: "",
                clubId: row.clubId,
                publisherId: this.$store.state.userInfo.id
            };
            this.showTweetFlag = true;
        },
        submitTweet() {
            if (!this.tweetForm.title || !this.tweetForm.content) {
                this.$message.warning("请填写推文标题和内容");
                return;
            }
            addNotices(this.tweetForm).then(resp => {
                this.$message.success("推文发布成功！");
                this.showTweetFlag = false;
            });
        },
        getPageInfo(pageIndex, pageSize) {
            this.loading = true;
            getPageActivities(pageIndex, pageSize, this.qryForm.clubId, this.qryForm.title).then(
                async (resp) => {
                    let activities = resp.data.data;
                    // Check user's participation status for each activity
                    if (this.userType == 2 && this.$store.state.userInfo) {
                        const userId = this.$store.state.userInfo.id;
                        // To avoid infinite loading if there are many activities, we only fetch participants if userType == 2
                        for (let i = 0; i < activities.length; i++) {
                            try {
                                const pResp = await getPageActivityParticipants(1, 10, activities[i].id, userId, null);
                                if (pResp.data && pResp.data.data && pResp.data.data.length > 0) {
                                    activities[i].myAuditStatus = pResp.data.data[0].auditStatus;
                                } else {
                                    activities[i].myAuditStatus = -1; // Not applied
                                }
                            } catch (e) {
                                activities[i].myAuditStatus = -1;
                            }
                        }
                    }

                    this.pageInfos = activities;
                    this.pageIndex = resp.data.pageIndex;
                    this.pageSize = resp.data.pageSize;
                    this.pageTotal = resp.data.pageTotal;
                    this.totalInfo = resp.data.count;

                    this.loading = false;
                }
            ).catch(() => {
                this.loading = false;
            });
        },
        getPageLikeInfo() {
            this.loading = true;
            getPageActivities(
                1,
                this.pageSize,
                this.qryForm.clubId,
                this.qryForm.title
            ).then(async (resp) => {
                let activities = resp.data.data;
                if (this.userType == 2 && this.$store.state.userInfo) {
                    const userId = this.$store.state.userInfo.id;
                    for (let i = 0; i < activities.length; i++) {
                        try {
                            const pResp = await getPageActivityParticipants(1, 10, activities[i].id, userId, null);
                            if (pResp.data && pResp.data.data && pResp.data.data.length > 0) {
                                activities[i].myAuditStatus = pResp.data.data[0].auditStatus;
                            } else {
                                activities[i].myAuditStatus = -1; // Not applied
                            }
                        } catch (e) {
                            activities[i].myAuditStatus = -1;
                        }
                    }
                }

                this.pageInfos = activities;
                this.pageIndex = resp.data.pageIndex;
                this.pageSize = resp.data.pageSize;
                this.totalInfo = resp.data.count;
                this.pageTotal = resp.data.pageTotal;
                this.loading = false;
            }).catch(() => {
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
            this.activitiesForm = {
                id: "",
                clubId: "",
                title: "",
                content: "",
                location: "",
                startTime: "",
                endTime: "",
            };
        },
        showAddWin() {
            this.initForm();
            this.showAddFlag = true;
        },
        active(id) {
            this.$prompt("请输入报名理由：", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                inputPattern: /.+/,
                inputErrorMessage: '报名理由不能为空'
            })
            .then(({ value }) => {
                applyJoinActivity(id, this.$store.state.userInfo.id, value).then((resp) => {
                    if (resp.code == 0) {
                        this.$message({
                            message: "报名成功，等待审核",
                            type: "success",
                        });
                        this.getPageInfo(this.pageIndex, this.pageSize);
                    } else {
                        this.$message({
                            message: resp.msg,
                            type: "warning",
                        });
                    }
                });
            })
            .catch(() => {
                this.$message({
                    type: "info",
                    message: "已取消报名",
                });
            });
        },
        addInfo() {
            addActivities(this.activitiesForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);

                this.showAddFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("删除活动将同时删除报名记录, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                delActivities(id).then((resp) => {
                    this.$message({
                        message: resp.msg,
                        type: "success",
                    });

                    this.getPageInfo(1, this.pageSize);
                });
            });
        },
        updInfo() {
            updActivities(this.activitiesForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);

                this.showUpdFlag = false;
            });
        },
    },
    mounted() {
        getLoginUser(this.$store.state.token).then((resp) => {
            this.userType = resp.data.type;
            this.$store.state.userInfo = resp.data;
            this.getPageInfo(1, this.pageSize);
        });
        
        getAllTeamList().then((resp) => {
            this.teams = resp.data;
            this.teamsList = resp.data;
        });
    },
};
</script>

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
                            v-model="qryForm.title"
                            placeholder="输入通知标题…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item v-if="userType !== 1">
                        <el-input
                            v-model="qryForm.clubId"
                            placeholder="输入社团ID…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="getPageLikeInfo()" style="font-size: 18px"
                            > 搜索</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>

        <el-card shadow="never">
            <div v-if="true" slot="header">
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
                        label="通知标题"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="clubId"
                        label="发布社团"
                    >
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.clubName" type="success">{{
                                scope.row.clubName
                            }}</el-tag>
                            <el-tag v-else type="warning">系统通知</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="publisherName"
                        label="发布人"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        label="发布时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="content"
                        label="通知详情"
                    ></el-table-column>
                    <el-table-column
                        v-if="userType == 1 || userType == 0"
                        align="center"
                        label="操作处理"
                        fixed="right"
                        width="160"
                    >
                        <template slot-scope="scope">
                            <div style="display: flex; justify-content: center; align-items: center; gap: 8px;">
                                <el-button
                                    v-if="userType == 1"
                                    type="primary"
                                    style="font-size: 14px"
                                    @click="showUpdWin(scope.row)"
                                >编辑</el-button>
                                <el-button
                                    type="danger"
                                    style="font-size: 14px"
                                    @click="delInfo(scope.row.id)"
                                >删除</el-button>
                            </div>
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
            <el-form label-width="90px" :model="noticesForm">
                <el-form-item label="通知标题">
                    <el-input
                        v-model="noticesForm.title"
                        placeholder="请输入通知标题…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="发布社团" v-if="userType !== 1">
                    <el-select
                        style="width: 100%"
                        v-model="noticesForm.clubId"
                        placeholder="请选择发布社团"
                    >
                        <el-option
                            v-for="(item, index) in teams"
                            :key="index"
                            :label="item.clubName"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="通知详情">
                    <el-input
                        type="textarea"
                        rows="5"
                        v-model="noticesForm.content"
                        placeholder="请输入通知详情…"
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
            <el-form label-width="90px" :model="noticesForm">
                <el-form-item label="通知标题">
                    <el-input
                        v-model="noticesForm.title"
                        placeholder="请输入通知标题…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="发布社团" v-if="userType !== 1">
                    <el-select
                        style="width: 100%"
                        v-model="noticesForm.clubId"
                        placeholder="请选择发布社团"
                    >
                        <el-option
                            v-for="(item, index) in teams"
                            :key="index"
                            :label="item.clubName"
                            :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="通知详情">
                    <el-input
                        type="textarea"
                        rows="6"
                        v-model="noticesForm.content"
                        placeholder="请输入通知详情…"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showUpdFlag = false" style="font-size: 18px"
                    >取 消</el-button
                >
                <el-button
                    type="primary"
                    @click="updInfo()"
                    style="font-size: 18px"
                    >确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<style>
</style>

<script>
import {
    getAllTeamList,
    getLoginUser,
    getPageNotices,
    addNotices,
    updNotices,
    delNotices,
} from "../../api";

export default {
    data() {
        return {
            teams: [],
            userType: "",
            myClubId: "",
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
                title: "",
            },
            noticesForm: {
                id: "",
                title: "",
                content: "",
                clubId: null,
                publisherId: null
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
            getPageNotices(pageIndex, pageSize, this.qryForm.clubId, this.qryForm.title).then(
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
            getPageNotices(
                1,
                this.pageSize,
                this.qryForm.clubId,
                this.qryForm.title
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
            this.noticesForm = {
                id: "",
                title: "",
                content: "",
                clubId: null,
                publisherId: this.$store.state.userInfo.id
            };
        },
        showAddWin() {
            this.initForm();
            if (this.userType === 1 && this.myClubId) {
                this.noticesForm.clubId = this.myClubId;
            }
            this.showAddFlag = true;
        },
        showUpdWin(row) {
            this.noticesForm = { ...row };
            if (this.userType === 1 && this.myClubId) {
                this.noticesForm.clubId = this.myClubId;
            }
            this.showUpdFlag = true;
        },
        addInfo() {
            if (this.userType === 1 && this.myClubId) {
                this.noticesForm.clubId = this.myClubId;
            }
            addNotices(this.noticesForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });
                this.getPageInfo(1, this.pageSize);
                this.showAddFlag = false;
            });
        },
        updInfo() {
            if (this.userType === 1 && this.myClubId) {
                this.noticesForm.clubId = this.myClubId;
            }
            updNotices(this.noticesForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });
                this.getPageInfo(1, this.pageSize);
                this.showUpdFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("即将删除相关信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                delNotices(id).then((resp) => {
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
            if (this.userType === 1) {
                getAllTeamList().then(teamResp => {
                    this.teams = teamResp.data;
                    let myTeam = teamResp.data.find(t => t.managerId === this.$store.state.userInfo.id || t.managerName === this.$store.state.userInfo.realName);
                    if (myTeam) {
                        this.myClubId = myTeam.id;
                        this.qryForm.clubId = myTeam.id;
                    }
                    this.getPageInfo(1, this.pageSize);
                });
            } else {
                getAllTeamList().then((resp) => {
                    this.teams = resp.data;
                });
                this.getPageInfo(1, this.pageSize);
            }
        });
    },
};
</script>
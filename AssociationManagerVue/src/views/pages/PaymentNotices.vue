<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item v-if="userType !== 1">
                        <el-input v-model="qryForm.clubId" placeholder="输入社团ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="qryForm.title" placeholder="输入缴费标题…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="warning" icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
                        <el-button v-if="true" type="primary" icon="el-icon-plus" @click="showAddWin()"></el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div>
                <el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)" :data="pageInfos" border>
                    <el-table-column align="center" type="index" label="序号"></el-table-column>
                    <el-table-column align="center" prop="clubName" label="发布社团"></el-table-column>
                    <el-table-column align="center" prop="title" label="缴费标题"></el-table-column>
                    <el-table-column align="center" prop="amount" label="缴费金额 (元)"></el-table-column>
                    <el-table-column align="center" label="发布时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="截止时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.deadline) }}
                        </template>
                    </el-table-column>
                    
                    <el-table-column v-if="true" align="center" label="操作处理" width="120" fixed="right">
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

        <el-dialog title="发布缴费通知" width="600px" :visible.sync="showAddFlag">
            <el-form label-width="90px" :model="pnForm">
                <el-form-item label="发布社团" v-if="userType !== 1">
                    <el-select style="width: 100%" v-model="pnForm.clubId" placeholder="请选择发布社团">
                        <el-option v-for="(item, index) in teams" :key="index" :label="item.clubName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费标题">
                    <el-input v-model="pnForm.title" placeholder="如：2024秋季会费" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="缴费金额">
                    <el-input v-model="pnForm.amount" type="number" placeholder="请输入金额" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="截止时间">
                    <el-date-picker style="width: 100%" v-model="pnForm.deadline" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择截止时间"></el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showAddFlag = false" style="font-size: 18px"> 取 消</el-button>
                <el-button type="primary" @click="addInfo()" style="font-size: 18px"> 发 布</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { getLoginUser, getPagePaymentNotices, addPaymentNotice, delPaymentNotice, getAllTeamList } from "../../api";

export default {
    data() {
        return {
            userType: "",
            myClubId: "",
            pageInfos: [],
            pageIndex: 1,
            pageSize: 10,
            pageTotal: 0,
            totalInfo: 0,
            loading: true,
            showAddFlag: false,
            teams: [],
            qryForm: {
                clubId: "",
                title: "",
            },
            pnForm: {
                clubId: "",
                title: "",
                amount: "",
                deadline: ""
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
            getPagePaymentNotices(pageIndex, pageSize, this.qryForm.clubId, this.qryForm.title).then((resp) => {
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
            this.pnForm = {
                clubId: "",
                title: "",
                amount: "",
                deadline: ""
            };
            if (this.userType === 1 && this.myClubId) {
                this.pnForm.clubId = this.myClubId;
            }
            this.showAddFlag = true;
        },
        addInfo() {
            if (this.userType === 1 && this.myClubId) {
                this.pnForm.clubId = this.myClubId;
            }
            addPaymentNotice(this.pnForm).then((resp) => {
                this.$message.success(resp.msg);
                this.getPageInfo(1, this.pageSize);
                this.showAddFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("确定要删除该缴费通知吗?", "提示", { type: "warning" }).then(() => {
                delPaymentNotice(id).then(resp => {
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
    }
};
</script>
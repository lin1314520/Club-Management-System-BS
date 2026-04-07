<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item v-if="userType == 0">
                        <el-input v-model="qryForm.userId" placeholder="输入申请人ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="qryForm.status" placeholder="请选择状态">
                            <el-option label="全部" value=""></el-option>
                            <el-option label="审批中" :value="0"></el-option>
                            <el-option label="已通过" :value="1"></el-option>
                            <el-option label="已拒绝" :value="2"></el-option>
                            <el-option label="已撤销" :value="3"></el-option>
                        </el-select>
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
                    <el-table-column align="center" prop="clubName" label="申请社团名称"></el-table-column>
                    <el-table-column align="center" prop="typeName" label="社团类型"></el-table-column>
                    <el-table-column align="center" prop="realName" label="申请人姓名"></el-table-column>
                    <el-table-column align="center" prop="description" label="申请理由/简介"></el-table-column>
                    <el-table-column align="center" label="申请时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.applyTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="状态">
                        <template slot-scope="scope">
                            <el-tag type="warning" v-if="scope.row.status == 0">审批中</el-tag>
                            <el-tag type="success" v-if="scope.row.status == 1">已通过</el-tag>
                            <el-tag type="danger" v-if="scope.row.status == 2">已拒绝</el-tag>
                            <el-tag type="info" v-if="scope.row.status == 3">已撤销</el-tag>
                        </template>
                    </el-table-column>
                    
                    <el-table-column v-if="userType == 0 || userType == 2" align="center" label="操作处理" width="150" fixed="right">
                        <template slot-scope="scope">
                            <!-- 管理员操作 -->
                            <el-button v-if="userType == 0 && scope.row.status == 0" type="success" size="mini" @click="audit(scope.row.id, 1)">通过</el-button>
                            <el-button v-if="userType == 0 && scope.row.status == 0" type="danger" size="mini" @click="audit(scope.row.id, 2)">拒绝</el-button>
                            <!-- 普通用户操作 -->
                            <el-button v-if="userType == 2 && scope.row.status == 0" type="danger" size="mini" @click="cancelApp(scope.row.id)">撤销</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[10, 20, 50]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="申请建社" width="600px" :visible.sync="showAddFlag">
            <el-form label-width="90px" :model="appForm">
                <el-form-item label="社团名称">
                    <el-input v-model="appForm.clubName" placeholder="请输入社团名称…" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="社团类型">
                    <el-select style="width: 100%" v-model="appForm.typeId" placeholder="请选择社团类型">
                        <el-option v-for="(item, index) in teamTypes" :key="index" :label="item.typeName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="申请理由">
                    <el-input v-model="appForm.description" type="textarea" rows="4" placeholder="请输入申请理由与社团简介…" autocomplete="off"></el-input>
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
import { getLoginUser, getPageClubApplications, addClubApplication, auditClubApplication, cancelClubApplication, getAllTypes } from "../../api";

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
            teamTypes: [],
            qryForm: {
                userId: "",
                status: "",
            },
            appForm: {
                clubName: "",
                typeId: "",
                description: "",
                userId: ""
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
            getPageClubApplications(pageIndex, pageSize, uId, this.qryForm.status).then((resp) => {
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
            this.appForm = {
                clubName: "",
                typeId: "",
                description: "",
                userId: this.$store.state.userInfo.id
            };
            this.showAddFlag = true;
        },
        addInfo() {
            addClubApplication(this.appForm).then((resp) => {
                this.$message.success(resp.msg);
                this.getPageInfo(1, this.pageSize);
                this.showAddFlag = false;
            });
        },
        audit(id, status) {
            auditClubApplication(id, status).then(resp => {
                this.$message.success(resp.msg);
                this.getPageInfo(this.pageIndex, this.pageSize);
            });
        },
        cancelApp(id) {
            this.$confirm("确定要撤销该申请吗?", "提示", { type: "warning" }).then(() => {
                cancelClubApplication(id).then(resp => {
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
        getAllTypes().then(resp => {
            this.teamTypes = resp.data;
        });
    }
};
</script>
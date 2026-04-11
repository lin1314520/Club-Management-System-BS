<template>
    <div class="info-wrapper">
        <el-card shadow="never">
            <div slot="header">
                <el-form :inline="true" :model="qryForm">
                    <el-form-item>
                        <el-input v-model="qryForm.clubId" placeholder="输入社团ID…" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="qryForm.status" placeholder="选择状态" clearable>
                            <el-option label="已完成" :value="1"></el-option>
                        </el-select>
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
                    <el-table-column align="center" prop="clubName" label="社团名称"></el-table-column>
                    <el-table-column align="center" prop="applicantName" label="申请人"></el-table-column>
                    <el-table-column align="center" prop="amount" label="提现金额 (元)"></el-table-column>
                    <el-table-column align="center" prop="reason" label="提现事由"></el-table-column>
                    <el-table-column align="center" label="状态">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.status === 1" type="success">已完成</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="提现时间">
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.applyTime) }}
                        </template>
                    </el-table-column>
                    
                    <el-table-column v-if="true" align="center" label="操作处理" width="160" fixed="right">
                        <template slot-scope="scope">
                            <template v-if="true">
                                <el-button type="danger" size="mini" @click="delInfo(scope.row.id)">删除</el-button>
                            </template>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[10, 20, 50]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="记录提现" width="600px" :visible.sync="showAddFlag">
            <el-form label-width="90px" :model="wrForm">
                <el-form-item label="所属社团">
                    <el-select style="width: 100%" v-model="wrForm.clubId" placeholder="请选择您的社团">
                        <el-option v-for="(item, index) in teams" :key="index" :label="item.clubName" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="提现金额">
                    <el-input v-model="wrForm.amount" type="number" placeholder="请输入提现金额" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="提现事由">
                    <el-input type="textarea" :rows="3" v-model="wrForm.reason" placeholder="请输入提现事由" autocomplete="off"></el-input>
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
import { getLoginUser, getPageWithdrawalRecords, addWithdrawalRecord, delWithdrawalRecord, getAllTeamList } from "../../api";

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
            teams: [],
            qryForm: {
                clubId: "",
                status: "",
            },
            wrForm: {
                clubId: "",
                amount: "",
                reason: ""
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
            getPageWithdrawalRecords(pageIndex, pageSize, this.qryForm.clubId, this.qryForm.status).then((resp) => {
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
            this.wrForm = {
                clubId: "",
                amount: "",
                reason: ""
            };
            this.showAddFlag = true;
        },
        addInfo() {
            const params = {
                ...this.wrForm,
                applicantId: this.$store.state.userInfo.id
            };
            addWithdrawalRecord(params).then((resp) => {
                this.$message.success(resp.msg);
                this.getPageInfo(1, this.pageSize);
                this.showAddFlag = false;
            });
        },
        delInfo(id) {
            this.$confirm("确定要删除该提现记录吗?", "提示", { type: "warning" }).then(() => {
                delWithdrawalRecord(id).then(resp => {
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
            
            if (this.userType == 1 || this.userType == 0) {
                getAllTeamList().then(resp => {
                    this.teams = resp.data;
                });
            }
        });
    }
};
</script>

<style scoped>
.info-wrapper {
    margin: 20px;
}
</style>

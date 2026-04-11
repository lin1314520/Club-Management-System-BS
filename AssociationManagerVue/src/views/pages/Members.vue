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
                            placeholder="输入社团ID…"
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
            <div v-if="true" slot="header">
                <!-- 可以加其他操作按钮 -->
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
                        prop="userId"
                        label="成员ID"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="userName"
                        label="用户账号"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="realName"
                        label="成员姓名"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="phone"
                        label="成员电话"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="clubName"
                        label="所属社团"
                    ></el-table-column>
                    <el-table-column align="center" label="成员角色">
                        <template slot-scope="scope">
                            <span v-if="scope.row.memberRole == 1" style="color: blue">社长</span>
                            <span v-if="scope.row.memberRole == 0">普通成员</span>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="成员状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.status == 0" style="color: orange">申请中</span>
                            <span v-if="scope.row.status == 1" style="color: green">已加入</span>
                            <span v-if="scope.row.status == 2" style="color: red">已拒绝</span>
                            <span v-if="scope.row.status == 3" style="color: gray">已退出</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="申请时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.applyTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="入团时间"
                    >
                        <template slot-scope="scope">
                            {{ formatDate(scope.row.joinTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="操作处理"
                        fixed="right"
                        width="140"
                    >
                        <template slot-scope="scope">
                            <el-button
                                type="danger"
                                style="font-size: 18px"
                                @click="delInfo(scope.row.id)"
                                >
                                删除</el-button
                            >
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
    </div>
</template>

<style>
</style>

<script>
import { getPageMembers, delMembers, getLoginUser } from "../../api";


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
                token: this.$store.state.token,
                clubId: "",
                status: 1, // 默认查询已加入的成员
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
            getPageMembers(pageIndex, pageSize, this.qryForm.clubId, this.qryForm.status).then(
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
            getPageMembers(
                1,
                this.pageSize,
                this.qryForm.clubId,
                this.qryForm.status
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
                pageSize,
            );
        },
        handleCurrentChange(pageIndex) {
            this.getPageInfo(
                pageIndex,
                this.pageSize,
            );
        },
        delInfo(id) {
            this.$confirm("移除成员将移除相关记录，确认继续吗?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                delMembers(id).then((resp) => {
                    if (resp.code == 0) {
                        this.$message({
                            message: resp.msg,
                            type: "success",
                        });

                        this.getPageInfo(1, this.pageSize);
                    } else {
                        this.$message({
                            message: resp.msg,
                            type: "warning",
                        });
                    }
                });
            });
        },
    },
    mounted() {
        this.getPageInfo(1, this.pageSize);
        getLoginUser(this.$store.state.token).then((resp) => {
            this.userType = resp.data.type;
        });
    },
};
</script>
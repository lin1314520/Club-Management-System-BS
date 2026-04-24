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
                            v-model="qryForm.username"
                            placeholder="输入用户账号…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.realName"
                            placeholder="输入真实姓名…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.phone"
                            placeholder="输入用户联系电话…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.studentId"
                            placeholder="输入学号…"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                            v-model="qryForm.address"
                            placeholder="输入专业班级…"
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
                        prop="id"
                        label="用户ID"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="username"
                        label="用户账号"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="realName"
                        label="真实姓名"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="studentId"
                        label="学号"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="address"
                        label="专业班级"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="phone"
                        label="联系电话"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="email"
                        label="邮箱"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="birthday"
                        label="出生日期"
                    >
                        <template slot-scope="scope">
                            {{ scope.row.birthday ? scope.row.birthday.substring(0, 10) : '' }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="用户类型">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.type == 0" type="danger">管理员</el-tag>
                            <el-tag v-else-if="scope.row.type == 1" type="warning">社长</el-tag>
                            <el-tag v-else type="info">普通用户</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.status == 1" style="color: green">正常</span>
                            <span v-if="scope.row.status == 0" style="color: red">禁用</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="createTime"
                        label="创建时间"
                        width="160"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        prop="updateTime"
                        label="更新时间"
                        width="160"
                    ></el-table-column>
                    <el-table-column
                        align="center"
                        label="操作处理"
                        fixed="right"
                        width="240"
                    >
                        <template slot-scope="scope">
                            <div style="display: flex; justify-content: center; align-items: center; gap: 10px;">
                                <el-dropdown @command="handleCommand($event, scope.row)" trigger="click">
                                  <el-button type="warning" style="font-size: 14px">
                                    转换角色<i class="el-icon-arrow-down el-icon--right"></i>
                                  </el-button>
                                  <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item v-if="scope.row.type !== 0" command="0">设为管理员</el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.type !== 1" command="1">设为社长</el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.type !== 2" command="2">设为普通用户</el-dropdown-item>
                                  </el-dropdown-menu>
                                </el-dropdown>
                                <el-button
                                    type="danger"
                                    @click="delInfo(scope.row.id, scope.row.type)"
                                    style="font-size: 14px"
                                    >
                                    删除</el-button
                                >
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
            <el-form label-width="90px" :model="usersForm">
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="用户账号">
                            <el-input
                                v-model="usersForm.username"
                                placeholder="请输入用户账号…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户密码">
                            <el-input
                                v-model="usersForm.password"
                                type="password"
                                placeholder="请输入用户密码…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="真实姓名">
                            <el-input
                                v-model="usersForm.realName"
                                placeholder="请输入真实姓名…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话">
                            <el-input
                                v-model="usersForm.phone"
                                placeholder="请输入联系电话…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="邮箱">
                            <el-input
                                v-model="usersForm.email"
                                placeholder="请输入邮箱…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态">
                            <el-select v-model="usersForm.status" placeholder="请选择状态">
                                <el-option label="正常" :value="1"></el-option>
                                <el-option label="禁用" :value="0"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="用户类型">
                            <el-select v-model="usersForm.type" placeholder="请选择类型">
                                <el-option label="管理员" :value="0"></el-option>
                                <el-option label="社长" :value="1"></el-option>
                                <el-option label="普通用户" :value="2"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="所属社团">
                            <el-select v-model="usersForm.clubId" placeholder="请选择社团" style="width: 100%">
                                <el-option label="无" value=""></el-option>
                                <el-option
                                    v-for="(item, index) in teamList"
                                    :key="index"
                                    :label="item.clubName"
                                    :value="item.id"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="出生日期">
                            <el-date-picker
                                v-model="usersForm.birthday"
                                type="date"
                                placeholder="选择出生日期"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="学号">
                            <el-input
                                v-model="usersForm.studentId"
                                placeholder="请输入学号…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="专业班级">
                            <el-input
                                v-model="usersForm.address"
                                placeholder="请输入专业班级…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
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
            <el-form label-width="90px" :model="usersForm">
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="用户账号">
                            <el-input
                                v-model="usersForm.username"
                                placeholder="请输入用户账号…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户密码">
                            <el-input
                                v-model="usersForm.password"
                                type="password"
                                placeholder="请输入用户密码…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="真实姓名">
                            <el-input
                                v-model="usersForm.realName"
                                placeholder="请输入真实姓名…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话">
                            <el-input
                                v-model="usersForm.phone"
                                placeholder="请输入联系电话…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="邮箱">
                            <el-input
                                v-model="usersForm.email"
                                placeholder="请输入邮箱…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态">
                            <el-select v-model="usersForm.status" placeholder="请选择状态">
                                <el-option label="正常" :value="1"></el-option>
                                <el-option label="禁用" :value="0"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="出生日期">
                            <el-date-picker
                                v-model="usersForm.birthday"
                                type="date"
                                placeholder="选择出生日期"
                                format="yyyy-MM-dd"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="学号">
                            <el-input
                                v-model="usersForm.studentId"
                                placeholder="请输入学号…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="15">
                    <el-col :span="12">
                        <el-form-item label="专业班级">
                            <el-input
                                v-model="usersForm.address"
                                placeholder="请输入专业班级…"
                                autocomplete="off"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showUpdFlag = false" style="font-size: 18px"
                    >
                    取 消</el-button
                >
                <el-button
                    type="primary"
                    @click="updInfo()"
                    style="font-size: 18px"
                    >
                    确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<style>
</style>

<script>
import {
    getPageUsers,
    addUsers,
    updUsers,
    delUsers,
    changeUserRole,
    getLoginUser,
    getAllTeamList,
} from "../../api";


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
            showUpdFlag: false,
            teamList: [],
            qryForm: {
                username: "",
                realName: "",
                phone: "",
                studentId: "",
                address: "",
            },
            usersForm: {
                id: "",
                username: "",
                password: "",
                realName: "",
                phone: "",
                email: "",
                status: 1,
                type: 2,
                birthday: null,
                studentId: "",
                address: "",
                clubId: "",
            },
        };
    },
    methods: {
        getPageInfo(pageIndex, pageSize, username, realName, phone, studentId, address) {
            getPageUsers(pageIndex, pageSize, username, realName, phone, studentId, address).then((resp) => {
                let dataList = resp.data.data;
                // 添加测试数据：如果缺少学号或专业班级，则自动填充Mock数据
                dataList.forEach((item, index) => {
                    if (!item.studentId) {
                        item.studentId = "2023" + String(index + 1).padStart(4, '0');
                    }
                    if (!item.address) {
                        item.address = "软件工程2023级" + (index % 3 + 1) + "班";
                    }
                });

                this.pageInfos = dataList;
                this.pageIndex = resp.data.pageIndex;
                this.pageSize = resp.data.pageSize;
                this.pageTotal = resp.data.pageTotal;
                this.totalInfo = resp.data.count;

                this.loading = false;
                
            });
        },
        getPageLikeInfo() {
            getPageUsers(
                1,
                this.pageSize,
                this.qryForm.username,
                this.qryForm.realName,
                this.qryForm.phone,
                this.qryForm.studentId,
                this.qryForm.address
            ).then((resp) => {
                let dataList = resp.data.data;
                // 添加测试数据：如果缺少学号或专业班级，则自动填充Mock数据
                dataList.forEach((item, index) => {
                    if (!item.studentId) {
                        item.studentId = "2023" + String(index + 1).padStart(4, '0');
                    }
                    if (!item.address) {
                        item.address = "软件工程2023级" + (index % 3 + 1) + "班";
                    }
                });

                this.pageInfos = dataList;
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
                this.qryForm.username,
                this.qryForm.realName,
                this.qryForm.phone,
                this.qryForm.studentId,
                this.qryForm.address
            );
        },
        handleCurrentChange(pageIndex) {
            this.getPageInfo(
                pageIndex,
                this.pageSize,
                this.qryForm.username,
                this.qryForm.realName,
                this.qryForm.phone,
                this.qryForm.studentId,
                this.qryForm.address
            );
        },
        initForm() {
            this.usersForm = {
                id: "",
                username: "",
                password: "",
                realName: "",
                phone: "",
                email: "",
                status: 1,
                type: 2,
                birthday: null,
                studentId: "",
                address: "",
                clubId: "",
            };
        },

        showAddWin() {
            this.initForm();
            this.showAddFlag = true;
        },
        showUpdWin(row) {
            this.usersForm = row;
            this.showUpdFlag = true;
        },
        addInfo() {
            this.usersForm["status"] = 1;

            addUsers(this.usersForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);

                this.showAddFlag = false;
            });
        },
        updInfo() {
            updUsers(this.usersForm).then((resp) => {
                this.$message({
                    message: resp.msg,
                    type: "success",
                });

                this.getPageInfo(1, this.pageSize);

                this.showUpdFlag = false;
            });
        },
        delInfo(id, type) {
            this.$confirm("即将删除相关信息, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                delUsers(id, type).then((resp) => {
                    this.$message({
                        message: resp.msg,
                        type: "success",
                    });

                    this.getPageInfo(1, this.pageSize);
                });
            });
        },
        handleCommand(command, row) {
            let targetType = parseInt(command);
            let roleName = targetType === 0 ? "管理员" : (targetType === 1 ? "社长" : "普通用户");
            this.$confirm(`是否将用户 ${row.realName || row.username} 设置为 ${roleName}?`, "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                changeUserRole(row.id, row.type, targetType).then((resp) => {
                    this.$message({
                        message: resp.msg,
                        type: "success",
                    });
                    this.getPageInfo(1, this.pageSize);
                });
            }).catch(() => {});
        },
    },
    mounted() {
        this.getPageInfo(1, this.pageSize);

        getLoginUser(this.$store.state.token).then((resp) => {
            this.userType = resp.data.type;
        });

        getAllTeamList().then((resp) => {
            this.teamList = resp.data;
        });
    },
};
</script>
<template>
    <div class="fater-body-show">
        <div style="margin-bottom: 20px;">
            <el-alert
                v-if="loginUser.type === 0"
                title="欢迎回来，尊敬的系统管理员！"
                description="您可以管理全站社团、系统用户及审核各类申请，掌控全局数据。"
                type="error"
                effect="dark"
                show-icon
                :closable="false"
                style="padding: 15px;">
            </el-alert>
            <el-alert
                v-if="loginUser.type === 1"
                :title="myClubName ? `欢迎回来，${myClubName} 社长！` : '欢迎回来，社长！'"
                description="您可以管理本社团成员、审核成员活动情况、发布活动及最新通知。"
                type="warning"
                effect="dark"
                show-icon
                :closable="false"
                style="padding: 15px;">
            </el-alert>
            <el-alert
                v-if="loginUser.type === 2"
                title="欢迎回来，亲爱的同学！"
                description="快去发现感兴趣的社团和活动吧，丰富你的校园生活。"
                type="success"
                effect="dark"
                show-icon
                :closable="false"
                style="padding: 15px;">
            </el-alert>
        </div>
        <el-row :gutter="15">
            <el-col :span="12">
                <el-card shadow="never">
                    <div>
                        <el-descriptions
                            title="个人资料"
                            :column="1"
                            size="small"
                            border
                        >
                            <el-descriptions-item>
                                <template slot="label"> 用户ID </template>
                                {{ loginUser.id }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 登录角色 </template>
                                <el-tag v-if="loginUser.type === 0"
                                    >系统管理员</el-tag
                                >
                                <el-tag v-if="loginUser.type === 1"
                                    >社团管理员</el-tag
                                >
                                <el-tag v-if="loginUser.type === 2"
                                    >普通用户</el-tag
                                >
                            </el-descriptions-item>
                            <el-descriptions-item v-if="loginUser.type === 1">
                                <template slot="label"> 所属社团 </template>
                                {{ myClubName || '暂无' }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 用户姓名 </template>
                                {{ loginUser.name }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 用户性别 </template>
                                {{ loginUser.gender }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 用户年龄 </template>
                                {{ loginUser.age }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 联系电话 </template>
                                {{ loginUser.phone }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 专业班级 </template>
                                {{ loginUser.address }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label"> 学号 </template>
                                {{ loginUser.studentId }}
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>
                </el-card>
                <img src="@/assets/06.jpg" style="width: 100%" />
            </el-col>
            <el-col :span="12">
                <el-card shadow="never">
                    <div
                        slot="header"
                        id="index-header"
                        style="font-size: 24px"
                    >
                        系统通知
                    </div>
                    <div>
                        <el-timeline>
                            <el-timeline-item
                                color="#E6A23C"
                                v-for="(item, index) in sysNotices"
                                :key="index"
                                :timestamp="formatDate(item.createTime)"
                                placement="top"
                            >
                                <el-card>
                                    <h4
                                        style="
                                            font-size: 16px;
                                            line-height: 28px;
                                            margin-bottom: 15px;
                                        "
                                    >
                                        {{ item.title }}
                                    </h4>
                                    <p
                                        style="
                                            font-size: 14px;
                                            line-height: 28px;
                                        "
                                    >
                                        {{ item.content }}
                                    </p>
                                </el-card>
                            </el-timeline-item>
                        </el-timeline>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<style>
</style>

<script>
import { getLoginUser, getSysNoticeList } from "../../api";


export default {
    computed: {
        myClubName() {
            return this.$store.state.user ? this.$store.state.user.clubName : '';
        }
    },
    data() {
        return {
            loginUser: {},
            sysNotices: [],
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
        }
    },
    mounted() {
        getLoginUser(this.$store.state.token).then((resp) => {
            this.loginUser = resp.data;
            this.$store.state.user = this.loginUser;
        });

        getSysNoticeList(this.$store.state.token).then((resp) => {
            this.sysNotices = resp.data;
        });
        setTimeout(() => {

            
        }, 100)
    },
};
</script>
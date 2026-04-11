<!--
 * @Description: 
 * @Author: Rabbiter
 * @Date: 2022-06-11 13:41:22
-->
<template>
    <el-container>
        <Nav></Nav>
        <el-container>
            <Menu :menuList="this.$store.state.menus.children"></Menu>
            <el-main class="fater-body">
                <el-breadcrumb
                    v-if="this.$router.currentRoute.path != '/index'"
                    class="fater-body-breadcrumb"
                >
                    <el-breadcrumb-item>系统首页</el-breadcrumb-item>
                    <el-breadcrumb-item>{{
                        this.$router.currentRoute.name
                    }}</el-breadcrumb-item>
                </el-breadcrumb>
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<style>
</style>

<script>
import Nav from "../components/nav.vue";
import Menu from "../components/menu.vue";
import { getLoginUser } from "../api";

export default {
    data() {
        return {};
    },
    methods: {},
    mounted() {
        // 模拟测试数据
        this.$store.state.user = { id: "U2023001", type: 0, username: "admin", name: "张三 (管理员)", gender: "男", age: 22, phone: "13800138000", address: "计算机学院软件工程专业1班" };
    },
    components: {
        Nav,
        Menu,
    },
};
</script>
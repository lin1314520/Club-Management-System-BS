import {getLoginUser} from "../api";

// 系统管理员
export const adminMenus = {
    path: '/home',
    name: 'home',
    component: require("../views/home.vue").default,
    children: [
        {
            path: '/index',
            name: '首页',
            icon: "iconfont icon-r-home",
            component: require("../views/pages/Index.vue").default
        },
        {
            path: '/users',
            name: '系统用户管理',
            icon: "iconfont icon-r-user1",
            component: require("../views/pages/Users.vue").default
        },
        {
            path: '/teamTypes',
            name: '社团类型管理',
            icon: "iconfont icon-r-list",
            component: require("../views/pages/TeamTypes.vue").default
        },
        {
            path: '/teams',
            name: '社团信息管理',
            icon: "iconfont icon-r-mark1",
            component: require("../views/pages/Teams.vue").default
        },
        {
            path: '/members',
            name: '社团成员管理',
            icon: "iconfont icon-r-user2",
            component: require("../views/pages/Members.vue").default
        },
        {
            path: '/applyLogs',
            name: '入社申请处理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ApplyLogs.vue").default
        },
        {
            path: '/clubApplications',
            name: '建社审批管理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ClubApplications.vue").default
        },
        {
            path: '/activities',
            name: '活动信息管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/Activities.vue").default
        },
        {
            path: '/activityTweets',
            name: '活动推文管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/ActivityTweets.vue").default
        },
        {
            path: '/signInRecords',
            name: '签到记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignInRecords.vue").default
        },
        {
            path: '/signOutRecords',
            name: '签退记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignOutRecords.vue").default
        },
        {
            path: '/activityFeedbacks',
            name: '活动反馈管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/ActivityFeedbacks.vue").default
        },
        {
            path: '/notices',
            name: '通知信息管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/Notices.vue").default
        },
        {
            path: '/payLogs',
            name: '查询缴费记录',
            icon: "iconfont icon-r-shield",
            component: require("../views/pages/PayLogs.vue").default
        },
        {
            path: '/withdrawalRecords',
            name: '提现记录管理',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/WithdrawalRecords.vue").default
        }
    ]
};

// 用户管理模块
export const memYMenus = {
    path: '/home',
    name: 'home',
    component: require("../views/home.vue").default,
    children: [
        {
            path: '/index',
            name: '首页',
            icon: "iconfont icon-r-home",
            component: require("../views/pages/Index.vue").default
        },
        {
            path: '/applyLogs',
            name: '入社申请管理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ApplyLogs.vue").default
        },
        {
            path: '/clubApplications',
            name: '申请创建新社团',
            icon: "iconfont icon-r-mark2",
            component: require("../views/pages/ClubApplications.vue").default
        },
        {
            path: '/teams',
            name: '浏览社团信息',
            icon: "iconfont icon-r-mark1",
            component: require("../views/pages/Teams.vue").default
        },
        {
            path: '/activities',
            name: '活动信息管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/Activities.vue").default
        },
        {
            path: '/activityTweets',
            name: '活动推文管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/ActivityTweets.vue").default
        },
        {
            path: '/activityParticipants',
            name: '活动报名审核',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/ActivityParticipants.vue").default
        },
        {
            path: '/signInRecords',
            name: '签到记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignInRecords.vue").default
        },
        {
            path: '/signOutRecords',
            name: '签退记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignOutRecords.vue").default
        },
        {
            path: '/activityFeedbacks',
            name: '活动反馈管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/ActivityFeedbacks.vue").default
        },
        {
            path: '/notices',
            name: '通知信息查询',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/Notices.vue").default
        },
        {
            path: '/payLogs',
            name: '缴费信息管理',
            icon: "iconfont icon-r-shield",
            component: require("../views/pages/PayLogs.vue").default
        }
    ]
};

// 社长管理模块
export const manMenus = {
    path: '/home',
    name: 'home',
    component: require("../views/home.vue").default,
    children: [
        {
            path: '/index',
            name: '首页',
            icon: "iconfont icon-r-home",
            component: require("../views/pages/Index.vue").default
        },
        {
            path: '/teamTypes',
            name: '社团类型浏览',
            icon: "iconfont icon-r-list",
            component: require("../views/pages/TeamTypes.vue").default
        },
        {
            path: '/applyLogs',
            name: '入社申请处理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ApplyLogs.vue").default
        },
        {
            path: '/members',
            name: '社团成员管理',
            icon: "iconfont icon-r-user2",
            component: require("../views/pages/Members.vue").default
        },
        {
            path: '/activities',
            name: '社团活动管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/Activities.vue").default
        },
        {
            path: '/activityTweets',
            name: '活动推文管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/ActivityTweets.vue").default
        },
        {
            path: '/activityParticipants',
            name: '活动人员审核',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/ActivityParticipants.vue").default
        },
        {
            path: '/signInRecords',
            name: '签到记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignInRecords.vue").default
        },
        {
            path: '/signOutRecords',
            name: '签退记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignOutRecords.vue").default
        },
        {
            path: '/activityFeedbacks',
            name: '查看活动心得',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/ActivityFeedbacks.vue").default
        },
        {
            path: '/notices',
            name: '社团通知管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/Notices.vue").default
        },
        {
            path: '/paymentNotices',
            name: '缴费通知发布',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/PaymentNotices.vue").default
        },
        {
            path: '/withdrawalRecords',
            name: '提现记录查看',
            icon: "iconfont icon-r-shield",
            component: require("../views/pages/WithdrawalRecords.vue").default
        },
        {
            path: '/payLogs',
            name: '社团缴费记录',
            icon: "iconfont icon-r-find",
            component: require("../views/pages/PayLogs.vue").default
        }
    ]
};

export const memNMenus = {
    path: '/home',
    name: 'home',
    component: require("../views/home.vue").default,
    children: [
        {
            path: '/index',
            name: '首页',
            icon: "iconfont icon-r-home",
            component: require("../views/pages/Index.vue").default
        }
    ]
};

// 合并所有菜单用于展示
export const allMenus = {
    path: '/home',
    name: 'home',
    component: require("../views/home.vue").default,
    children: [
        {
            path: '/index',
            name: '首页',
            icon: "iconfont icon-r-home",
            component: require("../views/pages/Index.vue").default
        },
        {
            path: '/users',
            name: '系统用户管理',
            icon: "iconfont icon-r-user1",
            component: require("../views/pages/Users.vue").default
        },
        {
            path: '/teamTypes',
            name: '社团类型管理',
            icon: "iconfont icon-r-list",
            component: require("../views/pages/TeamTypes.vue").default
        },
        {
            path: '/teams',
            name: '社团信息管理',
            icon: "iconfont icon-r-mark1",
            component: require("../views/pages/Teams.vue").default
        },
        {
            path: '/members',
            name: '社团成员管理',
            icon: "iconfont icon-r-user2",
            component: require("../views/pages/Members.vue").default
        },
        {
            path: '/applyLogs',
            name: '入社申请处理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ApplyLogs.vue").default
        },
        {
            path: '/clubApplications',
            name: '建社审批管理',
            icon: "iconfont icon-r-add",
            component: require("../views/pages/ClubApplications.vue").default
        },
        {
            path: '/activities',
            name: '活动信息管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/Activities.vue").default
        },
        {
            path: '/activityTweets',
            name: '活动推文管理',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/ActivityTweets.vue").default
        },
        {
            path: '/activityParticipants',
            name: '活动报名审核',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/ActivityParticipants.vue").default
        },
        {
            path: '/signInRecords',
            name: '签到记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignInRecords.vue").default
        },
        {
            path: '/signOutRecords',
            name: '签退记录查看',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/SignOutRecords.vue").default
        },
        {
            path: '/activityFeedbacks',
            name: '活动反馈管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/ActivityFeedbacks.vue").default
        },
        {
            path: '/notices',
            name: '通知信息管理',
            icon: "iconfont icon-r-edit",
            component: require("../views/pages/Notices.vue").default
        },
        {
            path: '/paymentNotices',
            name: '缴费通知发布',
            icon: "iconfont icon-r-paper",
            component: require("../views/pages/PaymentNotices.vue").default
        },
        {
            path: '/payLogs',
            name: '查询缴费记录',
            icon: "iconfont icon-r-shield",
            component: require("../views/pages/PayLogs.vue").default
        },
        {
            path: '/withdrawalRecords',
            name: '提现审批管理',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/WithdrawalRecords.vue").default
        }
    ]
};

export default function initMenu(router, store){

    let token = null;
    if(store.state.token){
        token = store.state.token;
    }else{
        token = sessionStorage.getItem("token");
        store.state.token = sessionStorage.getItem("token");
    }

    // 根据登录时选择的身份加载不同的菜单
    let type = sessionStorage.getItem("mock_login_type");
    if (type == '0') {
        router.addRoute(adminMenus);
        store.commit("setMenus", adminMenus);
    } else if (type == '1') {
        router.addRoute(manMenus);
        store.commit("setMenus", manMenus);
    } else {
        router.addRoute(memYMenus);
        store.commit("setMenus", memYMenus);
    }
}


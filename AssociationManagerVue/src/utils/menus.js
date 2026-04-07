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
            name: '提现审批管理',
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
            name: '入团申请管理',
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
            path: '/activityParticipants',
            name: '活动签到管理',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/ActivityParticipants.vue").default
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
            path: '/applyLogs',
            name: '入团申请处理',
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
            path: '/activityParticipants',
            name: '活动人员审核',
            icon: "iconfont icon-r-yes",
            component: require("../views/pages/ActivityParticipants.vue").default
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
            name: '社团提现申请',
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

export default function initMenu(router, store){

    let token = null;
    if(store.state.token){

        token = store.state.token;
    }else{

        token = sessionStorage.getItem("token");
        store.state.token = sessionStorage.getItem("token");
    }

    getLoginUser(token).then(resp =>{

        if(resp.data.type == 0){
            router.addRoute(adminMenus);
            store.commit("setMenus", adminMenus);
        }
    
        if(resp.data.type == 1){
            router.addRoute(manMenus);
            store.commit("setMenus", manMenus);
        }

        if(resp.data.type == 2){

            if(resp.data.status == 0){

                router.addRoute(memNMenus);
                store.commit("setMenus", memNMenus);
            }else{

                router.addRoute(memYMenus);
                store.commit("setMenus", memYMenus);
            }
            
        }
    });
}


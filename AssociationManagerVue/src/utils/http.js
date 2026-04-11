import axios from 'axios'
import qs from 'qs'
import {
	Message
} from 'element-ui'

axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';

const service = axios.create({
	// withCredentials: true,
	baseURL: 'http://localhost:9211/association',
	timeout: 15000 // 请求超时时间
})

// === MOCK ADAPTER START ===
// We intercept all requests and return mock data directly!
service.interceptors.request.use(config => {
	if(config.method === "post"){
		config.data = qs.stringify(config.data,  { indices: false });
	}
	return config;
}, error => {
	return Promise.reject(error)
})

const generateMockList = (url) => {
    let list = [];
    for (let i = 1; i <= 5; i++) {
        let obj = {
            id: String(i),
            title: "测试标题" + i,
            name: "测试名称" + i,
            clubName: "测试社团" + i,
            username: "test_user_" + i,
            userName: "test_user_" + i,
            realName: "测试用户" + i,
            typeName: "测试类型" + i,
            content: "这是一条测试内容数据" + i,
            description: "这是一条测试简介" + i,
            amount: 100 * i,
            status: i % 2 === 0 ? 1 : 0,
            auditStatus: String((i - 1) % 3), // "0", "1", "2"
            signInStatus: 1,
            signOutStatus: 0,
            activityStatus: 1,
            createTime: "2026-04-11 10:00:00",
            applyTime: "2026-04-11 10:00:00",
            auditTime: "2026-04-11 10:00:00",
            signTime: "2026-04-11 10:00:00",
            signInTime: "2026-04-11 10:00:00",
            signOutTime: "2026-04-11 11:00:00",
            startTime: "2026-04-11 10:00:00",
            endTime: "2026-04-12 10:00:00",
            joinTime: "2026-04-11 10:00:00",
            deadline: "2026-04-15 10:00:00",
            signInLaunchTime: "2026-04-11 10:00:00",
            signOutLaunchTime: "2026-04-11 10:00:00",
            replyTime: "2026-04-11 10:00:00",
            memberRole: i === 1 ? 1 : 0,
            total: 100 + i,
            type: i % 3,
            phone: "1380013800" + i,
            email: "test" + i + "@example.com",
            birthday: "2000-01-01",
            manager: "负责人" + i,
            location: "活动地点" + i,
            feedback: "审批意见" + i,
            reason: "申请原因" + i,
            joinReason: "入团理由" + i,
            applyReason: "报名理由" + i,
            activityId: String(i),
            clubId: String(i),
            userId: String(i),
            signInId: String(i),
            signOutId: String(i),
            joinAppId: String(i)
        };
        
        // Some specific adjustments
        if (url.includes('clubType')) {
            obj.typeName = ["学术科技类", "文化艺术类", "体育竞技类", "公益志愿类", "创新创业类"][i-1];
            obj.description = "这是一个" + obj.typeName + "的社团";
        }
        if (url.includes('clubInfo')) {
            obj.clubName = ["计算机协会", "吉他社", "羽毛球协会", "青年志愿者协会", "创客空间"][i-1];
            obj.typeId = String((i % 3) + 1);
            obj.typeName = "测试类型";
        }
        if (url.includes('activityInfo')) {
            obj.title = ["迎新晚会", "编程马拉松", "春季运动会", "志愿服务", "创业沙龙"][i-1];
            obj.activityTitle = obj.title;
        }
        if (url.includes('notificationInfo')) {
            obj.title = ["系统升级通知", "社团招新通知", "开会通知", "活动延期通知", "放假安排"][i-1];
            obj.noticeTitle = obj.title;
        }
        if (url.includes('withdrawalRecord')) {
            obj.reason = "购买活动物资及奖品";
            obj.applicantName = "申请人" + i;
        }
        if (url.includes('activityFeedback')) {
            obj.content = "这次活动办得非常好，学到了很多知识！";
            obj.replyContent = "收到反馈，感谢参与！";
        }
        if (url.includes('paymentNotice') || url.includes('paymentRecord')) {
            obj.title = ["春季会费", "秋季会费", "活动赞助", "场地费", "器材费"][i-1];
            obj.payTime = "2026-04-11 12:00:00";
        }
        if (url.includes('sysUser')) {
            obj.type = i % 3; // 0, 1, 2
        }
        if (url.includes('joinClubApplication')) {
            obj.auditStatus = String((i - 1) % 3);
        }
        
        list.push(obj);
    }
    return list;
};

// Hack adapter directly on the axios instance
const originalRequest = service.request;
service.request = function(config) {
    let url = config.url || '';
    
    return new Promise((resolve) => {
        setTimeout(() => {
            if (url.includes('login') || url.includes('/info')) {
                let type = sessionStorage.getItem("mock_login_type") || 2;
                resolve({
                    data: {
                        code: 1,
                        msg: "模拟登录成功",
                        data: {
                            id: "U2026001",
                            type: parseInt(type),
                            username: "admin",
                            name: type == 0 ? "张三 (管理员)" : (type == 1 ? "李四 (社长)" : "王五 (用户)"),
                            gender: "男",
                            age: 22,
                            birthday: "2000-01-01",
                            phone: "13800138000",
                            address: "测试地址",
                            token: "mock_token_for_test"
                        }
                    }
                });
                return;
            }
            
            if (url.includes('sys/notices')) {
                resolve({
                    data: {
                        code: 1,
                        msg: "成功",
                        data: generateMockList(url)
                    }
                });
                return;
            }

            if (url.includes('clubType/list') || url.includes('clubInfo/list')) {
                resolve({
                    data: {
                        code: 1,
                        msg: "成功",
                        data: generateMockList(url)
                    }
                });
                return;
            }

            if (url.includes('page') || url.includes('list') || url.includes('activeLogs')) {
                resolve({
                    data: {
                        code: 1,
                        msg: "成功",
                        data: {
                            data: generateMockList(url),
                            pageIndex: 1,
                            pageSize: 10,
                            pageTotal: 1,
                            count: 5,
                            total: 5 // some APIs use total
                        }
                    }
                });
                return;
            }

            // default success
            resolve({
                data: {
                    code: 1,
                    msg: "操作成功(模拟前端测试)",
                    data: null
                }
            });
        }, 100);
    });
};

['get', 'post', 'put', 'delete'].forEach(method => {
    service[method] = function(url, data, config) {
        return service.request(Object.assign(config || {}, {
            method,
            url,
            data
        }));
    };
});

export default service

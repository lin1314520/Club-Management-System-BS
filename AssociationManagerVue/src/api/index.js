import http from "../utils/http.js";

/** 基础系统认证相关接口 */
export function getSysNoticeList(token) {
	return http.get('/sys/notices', { params: { token: token } });
}
export function login(param) {
	return http.post('/login', param);
}
export function exit(token) {
	return http.get('/exit', { params: { token: token } });
}
export function getLoginUser(token) {
	return http.get('/info', { params: { token: token } });
}
export function checkUserPwd(token, oldPwd) {
	return http.get('/checkPwd', { params: { token: token, oldPwd: oldPwd } });
}
export function updLoginUserInfo(params) {
	return http.post('/info', params);
}
export function updLoginUserPwd(token, newPwd) {
	return http.post('/pwd', { token: token, password: newPwd });
}

/** 1. 系统用户管理接口 */
export function getPageUsers(pageIndex, pageSize, username, realName, phone) {
	return http.get('/sysUser/page', {
		params: { pageIndex, pageSize, username, realName, phone }
	});
}
export function addUsers(params) {
	return http.post('/sysUser/add', params);
}
export function updUsers(params) {
	return http.post('/sysUser/upd', params);
}
export function delUsers(id, type) {
	return http.post('/sysUser/del', { id: id, type: type });
}
export function changeUserRole(id, currentType, targetType) {
	return http.post('/sysUser/changeRole', { id, currentType, targetType });
}

/** 2. 社团类型管理接口 */
export function getAllTypes() {
	return http.get('/clubType/list');
}
export function getPageTeamTypes(pageIndex, pageSize, typeName) {
	return http.get('/clubType/page', {
		params: { pageIndex, pageSize, typeName }
	});
}
export function addTeamTypes(params) {
	return http.post('/clubType/add', params);
}
export function updTeamTypes(params) {
	return http.post('/clubType/upd', params);
}
export function delTeamTypes(id) {
	return http.post('/clubType/del', { id: id });
}

/** 3. 社团信息管理接口 */
export function getAllTeamList() {
	return http.get('/clubInfo/list');
}
export function getPageTeams(pageIndex, pageSize, clubName, typeId) {
	return http.get('/clubInfo/page', {
		params: { pageIndex, pageSize, clubName, typeId }
	});
}
export function addTeams(params) {
	return http.post('/clubInfo/add', params);
}
export function updTeams(params) {
	return http.post('/clubInfo/upd', params);
}
export function delTeams(id) {
	return http.post('/clubInfo/del', { id: id });
}

/** 4. 入团申请与成员管理接口 */
export function getPageMembers(pageIndex, pageSize, clubId, status) {
	return http.get('/clubMember/page', {
		params: { pageIndex, pageSize, clubId, status }
	});
}
export function applyJoinClub(clubId, userId) {
	return http.post('/clubMember/apply', { clubId, userId });
}
export function auditClubMember(id, status) {
	return http.post('/clubMember/audit', { id, status });
}
export function quitClub(clubId, userId) {
	return http.post('/clubMember/quit', { clubId, userId });
}
export function delMembers(id) {
	return http.post('/clubMember/del', { id: id });
}

/** 5. 活动信息管理接口 */
export function getPageActivities(pageIndex, pageSize, clubId, title) {
	return http.get('/activityInfo/page', {
		params: { pageIndex, pageSize, clubId, title }
	});
}
export function addActivities(params) {
	return http.post('/activityInfo/add', params);
}
export function updActivities(params) {
	return http.post('/activityInfo/upd', params);
}
export function delActivities(id) {
	return http.post('/activityInfo/del', { id: id });
}

/** 6. 通知信息管理接口 */
export function getPageNotices(pageIndex, pageSize, clubId, title) {
	return http.get('/notificationInfo/page', {
		params: { pageIndex, pageSize, clubId, title }
	});
}
export function addNotices(params) {
	return http.post('/notificationInfo/add', params);
}
export function updNotices(params) {
	return http.post('/notificationInfo/upd', params);
}
export function delNotices(id) {
	return http.post('/notificationInfo/del', { id: id });
}

/** 7. 缴费管理接口 */
// 缴费通知 (社长发布)
export function getPagePaymentNotices(pageIndex, pageSize, clubId, title) {
	return http.get('/paymentNotice/page', {
		params: { pageIndex, pageSize, clubId, title }
	});
}
export function addPaymentNotice(params) {
	return http.post('/paymentNotice/add', params);
}
export function delPaymentNotice(id) {
	return http.post('/paymentNotice/del', { id: id });
}

// 缴费记录 (用户缴纳与查询)
export function getPagePayLogs(pageIndex, pageSize, clubId, userId, status) {
	return http.get('/paymentRecord/page', {
		params: { pageIndex, pageSize, clubId, userId, status }
	});
}
export function addPayLogs(params) {
	return http.post('/paymentRecord/add', params);
}
export function payRecord(id) {
	return http.post('/paymentRecord/pay', { id });
}
export function delPayLogs(id) {
	return http.post('/paymentRecord/del', { id });
}

/** 8. 建社申请管理接口 */
export function getPageClubApplications(pageIndex, pageSize, userId, status) {
	return http.get('/clubApplication/page', {
		params: { pageIndex, pageSize, userId, status }
	});
}
export function addClubApplication(params) {
	return http.post('/clubApplication/add', params);
}
export function auditClubApplication(id, status) {
	return http.post('/clubApplication/audit', { id, status });
}
export function cancelClubApplication(id) {
	return http.post('/clubApplication/cancel', { id });
}
export function delClubApplication(id) {
	return http.post('/clubApplication/del', { id });
}

/** 9. 活动人员与签到接口 */
export function getActiveLogs(activityId) {
	return http.get('/activityParticipant/page', {
		params: { pageIndex: 1, pageSize: 1000, activityId }
	}).then(res => {
		return { data: res.data.data };
	});
}
export function getPageActivityParticipants(pageIndex, pageSize, activityId, userId, auditStatus) {
	return http.get('/activityParticipant/page', {
		params: { pageIndex, pageSize, activityId, userId, auditStatus }
	});
}
export function applyJoinActivity(activityId, userId) {
	return http.post('/activityParticipant/apply', { activityId, userId });
}
export function auditActivityParticipant(id, auditStatus) {
	return http.post('/activityParticipant/audit', { id, auditStatus });
}
export function signInActivity(id) {
	return http.post('/activityParticipant/signIn', { id });
}
export function signOutActivity(id) {
	return http.post('/activityParticipant/signOut', { id });
}
export function delActivityParticipant(id) {
	return http.post('/activityParticipant/del', { id });
}

/** 10. 活动反馈(心得)接口 */
export function getPageActivityFeedbacks(pageIndex, pageSize, activityId, userId) {
	return http.get('/activityFeedback/page', {
		params: { pageIndex, pageSize, activityId, userId }
	});
}
export function addActivityFeedback(params) {
	return http.post('/activityFeedback/add', params);
}
export function delActivityFeedback(id) {
	return http.post('/activityFeedback/del', { id });
}

/** 11. 提现申请管理接口 */
export function getPageWithdrawalRecords(pageIndex, pageSize, clubId, status) {
	return http.get('/withdrawalRecord/page', {
		params: { pageIndex, pageSize, clubId, status }
	});
}
export function addWithdrawalRecord(params) {
	return http.post('/withdrawalRecord/add', params);
}
export function auditWithdrawalRecord(id, status) {
	return http.post('/withdrawalRecord/audit', { id, status });
}
export function delWithdrawalRecord(id) {
	return http.post('/withdrawalRecord/del', { id });
}
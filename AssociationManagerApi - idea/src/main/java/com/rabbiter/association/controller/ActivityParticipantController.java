package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ActivityInfo;
import com.rabbiter.association.entity.ActivityParticipant;
import com.rabbiter.association.entity.ActivitySignIn;
import com.rabbiter.association.entity.ActivitySignOut;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ActivityInfoService;
import com.rabbiter.association.service.ActivityParticipantService;
import com.rabbiter.association.service.ActivitySignInService;
import com.rabbiter.association.service.ActivitySignOutService;
import com.rabbiter.association.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activityParticipant")
public class ActivityParticipantController extends BaseController {

    @Autowired
    private ActivityParticipantService activityParticipantService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ActivitySignInService activitySignInService;

    @Autowired
    private ActivitySignOutService activitySignOutService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long activityId, Long userId, Integer auditStatus) {
        QueryWrapper<ActivityParticipant> qw = new QueryWrapper<>();
        if (activityId != null) qw.eq("activity_id", activityId);
        if (userId != null) qw.eq("user_id", userId);
        if (auditStatus != null) qw.eq("audit_status", auditStatus);
        qw.orderByDesc("apply_time");

        Page<ActivityParticipant> page = activityParticipantService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ActivityParticipant pt : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", pt.getId());
            map.put("activityId", pt.getActivityId());
            map.put("userId", pt.getUserId());
            map.put("auditStatus", pt.getAuditStatus());
            map.put("applyTime", pt.getApplyTime());

            // 查询独立的签到和签退记录
            QueryWrapper<ActivitySignIn> inQw = new QueryWrapper<>();
            inQw.eq("activity_id", pt.getActivityId()).eq("user_id", pt.getUserId());
            ActivitySignIn signIn = activitySignInService.getOne(inQw);
            map.put("signInTime", signIn != null ? signIn.getSignInTime() : null);

            QueryWrapper<ActivitySignOut> outQw = new QueryWrapper<>();
            outQw.eq("activity_id", pt.getActivityId()).eq("user_id", pt.getUserId());
            ActivitySignOut signOut = activitySignOutService.getOne(outQw);
            map.put("signOutTime", signOut != null ? signOut.getSignOutTime() : null);

            ActivityInfo activity = activityInfoService.getById(pt.getActivityId());
            if (activity != null) {
                map.put("activityTitle", activity.getTitle());
                map.put("activityStatus", activity.getStatus());
            }

            SysUser user = sysUserService.getById(pt.getUserId());
            if (user != null) {
                map.put("userName", user.getUsername());
                map.put("realName", user.getRealName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/apply")
    public R apply(Long activityId, Long userId) {
        QueryWrapper<ActivityParticipant> qw = new QueryWrapper<>();
        qw.eq("activity_id", activityId).eq("user_id", userId);
        if (activityParticipantService.count(qw) > 0) {
            return R.warn("您已报名该活动");
        }

        ActivityParticipant pt = new ActivityParticipant();
        pt.setActivityId(activityId);
        pt.setUserId(userId);
        pt.setAuditStatus(0); // 待审核
        pt.setApplyTime(new java.util.Date());
        activityParticipantService.save(pt);
        return R.successMsg("报名成功");
    }

    @PostMapping("/audit")
    public R audit(Long id, Integer auditStatus) {
        ActivityParticipant pt = activityParticipantService.getById(id);
        if (pt == null) return R.error("报名记录不存在");
        
        pt.setAuditStatus(auditStatus);
        activityParticipantService.updateById(pt);
        return R.successMsg("审核操作成功");
    }

    @PostMapping("/signIn")
    public R signIn(Long id) {
        ActivityParticipant pt = activityParticipantService.getById(id);
        if (pt == null) return R.error("报名记录不存在");
        if (pt.getAuditStatus() != 1) return R.warn("报名未审核通过，无法签到");

        QueryWrapper<ActivitySignIn> qw = new QueryWrapper<>();
        qw.eq("activity_id", pt.getActivityId()).eq("user_id", pt.getUserId());
        if (activitySignInService.count(qw) > 0) return R.warn("您已经签到过了");

        ActivitySignIn signIn = new ActivitySignIn();
        signIn.setActivityId(pt.getActivityId());
        signIn.setUserId(pt.getUserId());
        signIn.setSignInTime(new java.util.Date());
        activitySignInService.save(signIn);
        return R.successMsg("签到成功");
    }

    @PostMapping("/signOut")
    public R signOut(Long id) {
        ActivityParticipant pt = activityParticipantService.getById(id);
        if (pt == null) return R.error("报名记录不存在");

        QueryWrapper<ActivitySignIn> inQw = new QueryWrapper<>();
        inQw.eq("activity_id", pt.getActivityId()).eq("user_id", pt.getUserId());
        if (activitySignInService.count(inQw) == 0) return R.warn("请先签到");

        QueryWrapper<ActivitySignOut> outQw = new QueryWrapper<>();
        outQw.eq("activity_id", pt.getActivityId()).eq("user_id", pt.getUserId());
        if (activitySignOutService.count(outQw) > 0) return R.warn("您已经签退过了");

        ActivitySignOut signOut = new ActivitySignOut();
        signOut.setActivityId(pt.getActivityId());
        signOut.setUserId(pt.getUserId());
        signOut.setSignOutTime(new java.util.Date());
        activitySignOutService.save(signOut);
        return R.successMsg("签退成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        activityParticipantService.removeById(id);
        return R.success();
    }
}
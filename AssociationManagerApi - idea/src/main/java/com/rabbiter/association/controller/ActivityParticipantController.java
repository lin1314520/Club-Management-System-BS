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
            // v2 changed auditStatus, and dropped signInTime/signOutTime
            map.put("auditStatus", pt.getAuditStatus());
            map.put("applyReason", pt.getApplyReason());
            map.put("feedback", pt.getFeedback());
            map.put("auditTime", pt.getAuditTime());
            map.put("applyTime", pt.getApplyTime());

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
    public R audit(Long id, Integer auditStatus, String feedback) {
        ActivityParticipant pt = activityParticipantService.getById(id);
        if (pt == null) return R.error("记录不存在");

        pt.setAuditStatus(auditStatus);
        pt.setFeedback(feedback);
        pt.setAuditTime(new java.util.Date());
        activityParticipantService.updateById(pt);
        return R.successMsg("审批成功");
    }

    // --- Note: Sign in/out logic has been moved to ActivitySignIn/Out entities ---

    @PostMapping("/del")
    public R delInfo(Long id) {
        activityParticipantService.removeById(id);
        return R.success();
    }
}
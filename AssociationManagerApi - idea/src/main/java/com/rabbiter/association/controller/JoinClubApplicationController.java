package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.entity.JoinClubApplication;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.ClubMemberService;
import com.rabbiter.association.service.JoinClubApplicationService;
import com.rabbiter.association.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/joinClubApplication")
public class JoinClubApplicationController {

    @Autowired
    private JoinClubApplicationService joinClubApplicationService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ClubInfoService clubInfoService;

    /**
     * 申请入社
     */
    @PostMapping("/apply")
    public R apply(Long clubId, Long userId, String joinReason) {
        // 判断是否已经在社团中
        QueryWrapper<ClubMember> memberQw = new QueryWrapper<>();
        memberQw.eq("club_id", clubId).eq("user_id", userId).eq("status", 1);
        if (clubMemberService.count(memberQw) > 0) {
            return R.warn("您已是该社团成员");
        }

        // 判断是否有正在审批中的记录
        QueryWrapper<JoinClubApplication> appQw = new QueryWrapper<>();
        appQw.eq("club_id", clubId).eq("user_id", userId).eq("audit_status", "0");
        if (joinClubApplicationService.count(appQw) > 0) {
            return R.warn("您已有申请正在审批中，请耐心等待");
        }

        JoinClubApplication app = new JoinClubApplication();
        app.setClubId(clubId);
        app.setUserId(userId);
        app.setJoinReason(joinReason);
        app.setAuditStatus("0"); // 待审核
        app.setApplyTime(new Date());
        
        joinClubApplicationService.save(app);
        return R.successMsg("申请提交成功，等待审核");
    }

    /**
     * 审批入社
     */
    @PostMapping("/audit")
    @Transactional
    public R audit(Long joinAppId, String auditStatus, String feedback) {
        JoinClubApplication app = joinClubApplicationService.getById(joinAppId);
        if (app == null) return R.error("申请记录不存在");

        if (!"0".equals(app.getAuditStatus())) {
            return R.warn("该申请已处理");
        }

        app.setAuditStatus(auditStatus);
        app.setAuditTime(new Date());
        if (feedback != null) {
            app.setFeedback(feedback);
        }
        joinClubApplicationService.updateById(app);

        // 如果审批通过 (1)，则向社团成员表插入一条正式记录
        if ("1".equals(auditStatus)) {
            ClubMember member = new ClubMember();
            member.setClubId(app.getClubId());
            member.setUserId(app.getUserId());
            member.setMemberRole(0); // 普通成员
            member.setStatus(1); // 正常
            member.setJoinTime(new Date());
            clubMemberService.save(member);
        }

        return R.successMsg("审批完成");
    }

    /**
     * 查询列表
     */
    @GetMapping("/list")
    public R list(Long clubId, Long userId, Integer page, Integer limit) {
        if (page == null) page = 1;
        if (limit == null) limit = 10;
        Page<JoinClubApplication> p = new Page<>(page, limit);
        QueryWrapper<JoinClubApplication> qw = new QueryWrapper<>();
        if (clubId != null) qw.eq("club_id", clubId);
        if (userId != null) qw.eq("user_id", userId);
        qw.orderByDesc("apply_time");
        
        Page<JoinClubApplication> pageResult = joinClubApplicationService.page(p, qw);
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        for (JoinClubApplication app : pageResult.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("joinAppId", app.getJoinAppId());
            map.put("clubId", app.getClubId());
            map.put("userId", app.getUserId());
            map.put("auditStatus", app.getAuditStatus());
            map.put("joinReason", app.getJoinReason());
            map.put("applyTime", app.getApplyTime());
            map.put("auditTime", app.getAuditTime());
            map.put("feedback", app.getFeedback());

            ClubInfo club = clubInfoService.getById(app.getClubId());
            if (club != null) map.put("clubName", club.getClubName());

            SysUser user = sysUserService.getById(app.getUserId());
            if (user != null) {
                map.put("username", user.getUsername());
                map.put("realName", user.getRealName());
            }
            resultList.add(map);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("total", pageResult.getTotal());
        res.put("data", resultList);
        return R.successData(res);
    }
}

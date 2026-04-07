package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubApplication;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.entity.ClubType;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubApplicationService;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.ClubMemberService;
import com.rabbiter.association.service.ClubTypeService;
import com.rabbiter.association.service.SysPresidentService;
import com.rabbiter.association.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubApplication")
public class ClubApplicationController extends BaseController {

    @Autowired
    private ClubApplicationService clubApplicationService;

    @Autowired
    private ClubTypeService clubTypeService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPresidentService sysPresidentService;

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private ClubMemberService clubMemberService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long userId, Integer status) {
        QueryWrapper<ClubApplication> qw = new QueryWrapper<>();
        if (userId != null) {
            qw.eq("user_id", userId);
        }
        if (status != null) {
            qw.eq("status", status);
        }
        qw.orderByDesc("apply_time");

        Page<ClubApplication> page = clubApplicationService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ClubApplication app : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("userId", app.getUserId());
            map.put("clubName", app.getClubName());
            map.put("typeId", app.getTypeId());
            map.put("description", app.getDescription());
            map.put("status", app.getStatus());
            map.put("applyTime", app.getApplyTime());
            map.put("auditTime", app.getAuditTime());

            ClubType type = clubTypeService.getById(app.getTypeId());
            if (type != null) map.put("typeName", type.getTypeName());

            SysUser user = sysUserService.getById(app.getUserId());
            if (user != null) {
                map.put("userName", user.getUsername());
                map.put("realName", user.getRealName());
            } else {
                // 如果用户已经被升级成了社长，可能会在 sys_president 表中
                SysPresident pres = sysPresidentService.getById(app.getUserId());
                if (pres != null) {
                    map.put("userName", pres.getUsername());
                    map.put("realName", pres.getRealName());
                }
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(ClubApplication clubApplication) {
        clubApplication.setStatus(0); // 审批中
        clubApplication.setApplyTime(new java.util.Date());
        clubApplicationService.save(clubApplication);
        return R.successMsg("提交申请成功");
    }

    @PostMapping("/audit")
    public R audit(Long id, Integer status) {
        ClubApplication app = clubApplicationService.getById(id);
        if (app == null) return R.error("申请记录不存在");
        
        app.setStatus(status);
        app.setAuditTime(new java.util.Date());
        clubApplicationService.updateById(app);
        
        if (status == 1) { // 审批通过
            // 自动插入社团信息
            ClubInfo club = new ClubInfo();
            club.setClubName(app.getClubName());
            club.setTypeId(app.getTypeId());
            club.setDescription(app.getDescription());
            club.setStatus(1); // 正常
            club.setCreateTime(new java.util.Date());
            clubInfoService.save(club);

            // 将申请人升级为社长
            SysUser commonUser = sysUserService.getById(app.getUserId());
            Long presidentId = app.getUserId();
            if (commonUser != null) {
                SysPresident president = new SysPresident();
                president.setUsername(commonUser.getUsername());
                president.setPassword(commonUser.getPassword());
                president.setRealName(commonUser.getRealName());
                president.setPhone(commonUser.getPhone());
                president.setEmail(commonUser.getEmail());
                president.setStatus(commonUser.getStatus());
                president.setCreateTime(new java.util.Date());
                sysPresidentService.save(president);
                sysUserService.removeById(commonUser.getId());
                presidentId = president.getId();
            }

            // 插入社团成员表 (作为社长)
            ClubMember member = new ClubMember();
            member.setClubId(club.getId());
            member.setUserId(presidentId); // 指向 sys_president 的 ID
            member.setMemberRole(1); // 社长
            member.setStatus(1); // 已加入
            member.setJoinTime(new java.util.Date());
            clubMemberService.save(member);
        }

        return R.successMsg("审批操作成功");
    }

    @PostMapping("/cancel")
    public R cancel(Long id) {
        ClubApplication app = clubApplicationService.getById(id);
        if (app == null) return R.error("申请记录不存在");
        
        if (app.getStatus() != 0) {
            return R.warn("仅审批中的申请可撤销");
        }
        app.setStatus(3); // 撤销
        clubApplicationService.updateById(app);
        return R.successMsg("撤销成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        clubApplicationService.removeById(id);
        return R.success();
    }
}
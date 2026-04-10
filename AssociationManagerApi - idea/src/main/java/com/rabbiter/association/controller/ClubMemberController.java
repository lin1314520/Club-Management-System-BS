package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubMemberService;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.service.SysPresidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubMember")
public class ClubMemberController extends BaseController {

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPresidentService sysPresidentService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long clubId, Integer status) {
        QueryWrapper<ClubMember> qw = new QueryWrapper<>();
        if (clubId != null) {
            qw.eq("club_id", clubId);
        }
        if (status != null) {
            qw.eq("status", status);
        }
        qw.orderByDesc("apply_time");

        Page<ClubMember> page = clubMemberService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ClubMember member : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", member.getId());
            map.put("clubId", member.getClubId());
            map.put("userId", member.getUserId());
            map.put("memberRole", member.getMemberRole());
            map.put("status", member.getStatus());
            map.put("applyTime", member.getApplyTime());
            map.put("joinTime", member.getJoinTime());

            ClubInfo club = clubInfoService.getById(member.getClubId());
            if (club != null) map.put("clubName", club.getClubName());

            if (member.getMemberRole() != null && member.getMemberRole() == 1) {
                SysPresident user = sysPresidentService.getById(member.getUserId());
                if (user != null) {
                    map.put("userName", user.getUsername());
                    map.put("realName", user.getRealName());
                    map.put("phone", user.getPhone());
                }
            } else {
                SysUser user = sysUserService.getById(member.getUserId());
                if (user != null) {
                    map.put("userName", user.getUsername());
                    map.put("realName", user.getRealName());
                    map.put("phone", user.getPhone());
                }
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    // V2: club_member is for active members only, JoinClubApplication is for applying
    @PostMapping("/add")
    public R addMember(ClubMember clubMember) {
        // Here we just insert directly if needed, or this should go through JoinClubApplication
        clubMember.setStatus(1); // 1-正常
        clubMember.setJoinTime(new java.util.Date());
        clubMemberService.save(clubMember);
        return R.successMsg("操作成功");
    }

    @PostMapping("/audit")
    public R audit(Long id, Integer status) {
        // V2 NOTE: In V2, audit should be done on join_club_application, not club_member. 
        // This is a placeholder/legacy method if frontend is not updated yet.
        ClubMember member = clubMemberService.getById(id);
        if (member == null) return R.error("记录不存在");
        
        member.setStatus(status);
        if (status == 1) { // 通过
            member.setJoinTime(new java.util.Date());
            // 如果是普通成员，更新其 member_status 为 1
            if (member.getMemberRole() == null || member.getMemberRole() == 0) {
                SysUser user = sysUserService.getById(member.getUserId());
                if (user != null) {
                    user.setMemberStatus(1);
                    sysUserService.updateById(user);
                }
            }
        }
        clubMemberService.updateById(member);
        return R.successMsg("审核操作成功");
    }

    @PostMapping("/quit")
    public R quit(Long clubId, Long userId) {
        QueryWrapper<ClubMember> qw = new QueryWrapper<>();
        qw.eq("club_id", clubId).eq("user_id", userId);
        qw.last("limit 1");
        ClubMember exist = clubMemberService.getOne(qw);
        if (exist == null) return R.error("记录不存在");

        exist.setStatus(3); // 已退出
        clubMemberService.updateById(exist);

        // 检查该用户是否还有其他正常社团
        if (exist.getMemberRole() == null || exist.getMemberRole() == 0) {
            QueryWrapper<ClubMember> countQw = new QueryWrapper<>();
            countQw.eq("user_id", userId).eq("status", 1).ne("member_id", exist.getId());
            if (clubMemberService.count(countQw) == 0) {
                SysUser user = sysUserService.getById(userId);
                if (user != null) {
                    user.setMemberStatus(0);
                    sysUserService.updateById(user);
                }
            }
        }

        return R.successMsg("退出成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        clubMemberService.removeById(id);
        return R.successMsg("删除成功");
    }
}
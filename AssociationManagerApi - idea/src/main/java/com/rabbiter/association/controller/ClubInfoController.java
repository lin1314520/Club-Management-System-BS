package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.entity.ClubType;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.ClubMemberService;
import com.rabbiter.association.service.ClubTypeService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubInfo")
public class ClubInfoController extends BaseController {

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private ClubTypeService clubTypeService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/info")
    public R getInfo(Long id) {
        return R.successData(clubInfoService.getOne(id));
    }

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, ClubInfo clubInfo) {
        QueryWrapper<ClubInfo> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clubInfo.getClubName())) {
            qw.like("club_name", clubInfo.getClubName());
        }
        if (clubInfo.getTypeId() != null) {
            qw.eq("type_id", clubInfo.getTypeId());
        }
        qw.orderByDesc("create_time");

        Page<ClubInfo> page = clubInfoService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ClubInfo info : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", info.getId());
            map.put("clubName", info.getClubName());
            map.put("typeId", info.getTypeId());
            map.put("description", info.getDescription());
            map.put("status", info.getStatus());
            map.put("createTime", info.getCreateTime());

            // Type Name
            ClubType type = clubTypeService.getOne(info.getTypeId());
            if (type != null) {
                map.put("typeName", type.getTypeName());
            }

            // Manager (President)
            QueryWrapper<ClubMember> memQw = new QueryWrapper<>();
            memQw.eq("club_id", info.getId()).eq("member_role", 1);
            ClubMember president = clubMemberService.getOne(memQw);
            if (president != null) {
                SysUser user = sysUserService.getOne(president.getUserId());
                if (user != null) {
                    map.put("managerName", user.getRealName());
                }
            }

            // Total members
            QueryWrapper<ClubMember> totalQw = new QueryWrapper<>();
            totalQw.eq("club_id", info.getId()).eq("status", 1);
            map.put("total", clubMemberService.count(totalQw));

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @GetMapping("/list")
    public R getList() {
        return R.successData(clubInfoService.list());
    }

    @PostMapping("/add")
    public R addInfo(ClubInfo clubInfo) {
        clubInfo.setCreateTime(new java.util.Date());
        if (clubInfo.getStatus() == null) clubInfo.setStatus(1);
        clubInfoService.save(clubInfo);
        return R.success();
    }

    @PostMapping("/upd")
    public R updInfo(ClubInfo clubInfo) {
        clubInfoService.updateById(clubInfo);
        return R.success();
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        clubInfoService.removeById(id);
        return R.success();
    }
}
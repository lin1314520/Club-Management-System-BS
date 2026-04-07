package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.entity.WithdrawalRecord;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.SysPresidentService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.service.WithdrawalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/withdrawalRecord")
public class WithdrawalRecordController extends BaseController {

    @Autowired
    private WithdrawalRecordService withdrawalRecordService;

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPresidentService sysPresidentService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long clubId, Integer status) {
        QueryWrapper<WithdrawalRecord> qw = new QueryWrapper<>();
        if (clubId != null) qw.eq("club_id", clubId);
        if (status != null) qw.eq("status", status);
        qw.orderByDesc("apply_time");

        Page<WithdrawalRecord> page = withdrawalRecordService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (WithdrawalRecord wr : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", wr.getId());
            map.put("clubId", wr.getClubId());
            map.put("applicantId", wr.getApplicantId());
            map.put("amount", wr.getAmount());
            map.put("reason", wr.getReason());
            map.put("status", wr.getStatus());
            map.put("applyTime", wr.getApplyTime());

            ClubInfo club = clubInfoService.getById(wr.getClubId());
            if (club != null) map.put("clubName", club.getClubName());

            SysPresident pres = sysPresidentService.getById(wr.getApplicantId());
            if (pres != null) {
                map.put("applicantName", pres.getRealName());
            } else {
                SysUser user = sysUserService.getById(wr.getApplicantId());
                if (user != null) {
                    map.put("applicantName", user.getRealName());
                }
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(WithdrawalRecord record) {
        record.setStatus(1); // 提现直接完成，不需要审批
        record.setApplyTime(new java.util.Date());
        withdrawalRecordService.save(record);
        return R.successMsg("提现已完成");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        withdrawalRecordService.removeById(id);
        return R.successMsg("删除成功");
    }
}
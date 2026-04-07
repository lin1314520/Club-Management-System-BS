package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.entity.WithdrawalRecord;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.service.WithdrawalRecordService;
import com.rabbiter.association.utils.DateUtils;
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
            map.put("auditTime", wr.getAuditTime());

            ClubInfo club = clubInfoService.getOne(wr.getClubId());
            if (club != null) map.put("clubName", club.getClubName());

            SysUser user = sysUserService.getOne(wr.getApplicantId());
            if (user != null) {
                map.put("applicantName", user.getRealName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(WithdrawalRecord record) {
        record.setStatus(0); // 审批中
        record.setApplyTime(new java.util.Date());
        withdrawalRecordService.save(record);
        return R.successMsg("提现申请已提交");
    }

    @PostMapping("/audit")
    public R audit(Long id, Integer status) {
        WithdrawalRecord record = withdrawalRecordService.getOne(id);
        if (record == null) return R.error("申请记录不存在");
        
        record.setStatus(status);
        record.setAuditTime(new java.util.Date());
        withdrawalRecordService.updateById(record);
        return R.successMsg("审批操作成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        withdrawalRecordService.removeById(id);
        return R.successMsg("删除成功");
    }
}
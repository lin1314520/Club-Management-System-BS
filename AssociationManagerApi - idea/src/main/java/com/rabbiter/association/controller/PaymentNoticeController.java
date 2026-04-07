package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.entity.PaymentNotice;
import com.rabbiter.association.entity.PaymentRecord;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.ClubMemberService;
import com.rabbiter.association.service.PaymentNoticeService;
import com.rabbiter.association.service.PaymentRecordService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paymentNotice")
public class PaymentNoticeController extends BaseController {

    @Autowired
    private PaymentNoticeService paymentNoticeService;

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private ClubMemberService clubMemberService;

    @Autowired
    private PaymentRecordService paymentRecordService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, PaymentNotice notice) {
        QueryWrapper<PaymentNotice> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(notice.getTitle())) {
            qw.like("title", notice.getTitle());
        }
        if (notice.getClubId() != null) {
            qw.eq("club_id", notice.getClubId());
        }
        qw.orderByDesc("create_time");

        Page<PaymentNotice> page = paymentNoticeService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (PaymentNotice pn : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", pn.getId());
            map.put("clubId", pn.getClubId());
            map.put("title", pn.getTitle());
            map.put("amount", pn.getAmount());
            map.put("deadline", pn.getDeadline());
            map.put("createTime", pn.getCreateTime());

            ClubInfo club = clubInfoService.getOne(pn.getClubId());
            if (club != null) map.put("clubName", club.getClubName());

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(PaymentNotice notice) {
        notice.setCreateTime(new java.util.Date());
        paymentNoticeService.save(notice);

        // Fetch all joined members of the club (status = 1)
        QueryWrapper<ClubMember> memberQw = new QueryWrapper<>();
        memberQw.eq("club_id", notice.getClubId()).eq("status", 1);
        List<ClubMember> members = clubMemberService.list(memberQw);

        // Generate an unpaid record for each member
        List<PaymentRecord> records = new ArrayList<>();
        for (ClubMember member : members) {
            PaymentRecord pr = new PaymentRecord();
            pr.setNoticeId(notice.getId());
            pr.setUserId(member.getUserId());
            pr.setAmount(notice.getAmount());
            pr.setStatus(0); // 0: 未缴费
            records.add(pr);
        }
        
        if (!records.isEmpty()) {
            paymentRecordService.saveBatch(records);
        }

        return R.successMsg("发布缴费通知成功，已生成成员账单");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        paymentNoticeService.removeById(id);
        return R.successMsg("删除成功");
    }
}
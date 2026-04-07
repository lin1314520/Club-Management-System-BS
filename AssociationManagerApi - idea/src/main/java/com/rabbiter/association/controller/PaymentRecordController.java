package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.PaymentNotice;
import com.rabbiter.association.entity.PaymentRecord;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.PaymentNoticeService;
import com.rabbiter.association.service.PaymentRecordService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paymentRecord")
public class PaymentRecordController extends BaseController {

    @Autowired
    private PaymentRecordService paymentRecordService;

    @Autowired
    private PaymentNoticeService paymentNoticeService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long clubId, Long userId, Integer status) {
        QueryWrapper<PaymentRecord> qw = new QueryWrapper<>();
        if (userId != null) qw.eq("user_id", userId);
        if (status != null) qw.eq("status", status);

        if (clubId != null) {
            QueryWrapper<PaymentNotice> noticeQw = new QueryWrapper<>();
            noticeQw.eq("club_id", clubId);
            List<PaymentNotice> notices = paymentNoticeService.list(noticeQw);
            if (notices.isEmpty()) {
                return R.successData(new PageData(pageIndex, pageSize, 0L, new ArrayList<>()));
            }
            List<Long> noticeIds = new ArrayList<>();
            for (PaymentNotice notice : notices) {
                noticeIds.add(notice.getId());
            }
            qw.in("notice_id", noticeIds);
        }

        Page<PaymentRecord> page = paymentRecordService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (PaymentRecord pr : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", pr.getId());
            map.put("noticeId", pr.getNoticeId());
            map.put("userId", pr.getUserId());
            map.put("amount", pr.getAmount());
            map.put("status", pr.getStatus());
            map.put("payTime", pr.getPayTime());

            PaymentNotice notice = paymentNoticeService.getOne(pr.getNoticeId());
            if (notice != null) map.put("noticeTitle", notice.getTitle());

            SysUser user = sysUserService.getOne(pr.getUserId());
            if (user != null) {
                map.put("userName", user.getUsername());
                map.put("realName", user.getRealName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/pay")
    public R pay(Long id) {
        PaymentRecord pr = paymentRecordService.getOne(id);
        if (pr == null) return R.error("记录不存在");
        
        if (pr.getStatus() == 1) return R.warn("该记录已缴费");

        pr.setStatus(1);
        pr.setPayTime(new java.util.Date());
        paymentRecordService.updateById(pr);
        return R.successMsg("缴费成功");
    }

    @PostMapping("/add")
    public R addInfo(PaymentRecord record) {
        if (record.getStatus() == null) record.setStatus(0);
        paymentRecordService.save(record);
        return R.successMsg("生成缴费记录成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        paymentRecordService.removeById(id);
        return R.successMsg("删除成功");
    }
}
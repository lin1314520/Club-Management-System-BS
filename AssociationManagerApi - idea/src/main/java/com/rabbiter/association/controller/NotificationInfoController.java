package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.entity.NotificationInfo;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.service.NotificationInfoService;
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
@RequestMapping("/notificationInfo")
public class NotificationInfoController extends BaseController {

    @Autowired
    private NotificationInfoService notificationInfoService;

    @Autowired
    private ClubInfoService clubInfoService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/info")
    public R getInfo(Long id) {
        return R.successData(notificationInfoService.getOne(id));
    }

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, NotificationInfo notificationInfo) {
        QueryWrapper<NotificationInfo> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(notificationInfo.getTitle())) {
            qw.like("title", notificationInfo.getTitle());
        }
        if (notificationInfo.getClubId() != null) {
            qw.eq("club_id", notificationInfo.getClubId());
        }
        qw.orderByDesc("create_time");

        Page<NotificationInfo> page = notificationInfoService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (NotificationInfo info : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", info.getId());
            map.put("clubId", info.getClubId());
            map.put("title", info.getTitle());
            map.put("content", info.getContent());
            map.put("publisherId", info.getPublisherId());
            map.put("createTime", info.getCreateTime());

            if (info.getClubId() != null) {
                ClubInfo club = clubInfoService.getOne(info.getClubId());
                if (club != null) map.put("clubName", club.getClubName());
            } else {
                map.put("clubName", "系统通知");
            }

            SysUser user = sysUserService.getOne(info.getPublisherId());
            if (user != null) {
                map.put("publisherName", user.getRealName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(NotificationInfo notificationInfo) {
        notificationInfo.setCreateTime(new java.util.Date());
        notificationInfoService.save(notificationInfo);
        return R.successMsg("发布通知成功");
    }

    @PostMapping("/upd")
    public R updInfo(NotificationInfo notificationInfo) {
        notificationInfoService.updateById(notificationInfo);
        return R.successMsg("更新通知成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        notificationInfoService.removeById(id);
        return R.successMsg("删除通知成功");
    }
}
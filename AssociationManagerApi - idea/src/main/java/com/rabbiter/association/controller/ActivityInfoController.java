package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ActivityInfo;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ActivityInfoService;
import com.rabbiter.association.service.ClubInfoService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activityInfo")
public class ActivityInfoController extends BaseController {

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private ClubInfoService clubInfoService;

    @GetMapping("/info")
    public R getInfo(Long id) {
        return R.successData(activityInfoService.getOne(id));
    }

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, ActivityInfo activityInfo) {
        QueryWrapper<ActivityInfo> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(activityInfo.getTitle())) {
            qw.like("title", activityInfo.getTitle());
        }
        if (activityInfo.getClubId() != null) {
            qw.eq("club_id", activityInfo.getClubId());
        }
        qw.orderByDesc("create_time");

        Page<ActivityInfo> page = activityInfoService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ActivityInfo info : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", info.getId());
            map.put("clubId", info.getClubId());
            map.put("title", info.getTitle());
            map.put("content", info.getContent());
            map.put("location", info.getLocation());
            map.put("startTime", info.getStartTime());
            map.put("endTime", info.getEndTime());
            map.put("status", info.getStatus());
            map.put("createTime", info.getCreateTime());

            ClubInfo club = clubInfoService.getOne(info.getClubId());
            if (club != null) {
                map.put("clubName", club.getClubName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(ActivityInfo activityInfo) {
        activityInfo.setCreateTime(new java.util.Date());
        if (activityInfo.getStatus() == null) activityInfo.setStatus(0);
        activityInfoService.save(activityInfo);
        return R.successMsg("发布活动成功");
    }

    @PostMapping("/upd")
    public R updInfo(ActivityInfo activityInfo) {
        activityInfoService.updateById(activityInfo);
        return R.successMsg("更新活动成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        activityInfoService.removeById(id);
        return R.successMsg("删除活动成功");
    }
}
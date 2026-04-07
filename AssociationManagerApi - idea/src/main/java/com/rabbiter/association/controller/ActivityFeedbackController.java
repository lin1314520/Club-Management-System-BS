package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ActivityFeedback;
import com.rabbiter.association.entity.ActivityInfo;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ActivityFeedbackService;
import com.rabbiter.association.service.ActivityInfoService;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activityFeedback")
public class ActivityFeedbackController extends BaseController {

    @Autowired
    private ActivityFeedbackService activityFeedbackService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, Long activityId, Long userId) {
        QueryWrapper<ActivityFeedback> qw = new QueryWrapper<>();
        if (activityId != null) qw.eq("activity_id", activityId);
        if (userId != null) qw.eq("user_id", userId);
        qw.orderByDesc("create_time");

        Page<ActivityFeedback> page = activityFeedbackService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ActivityFeedback fb : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", fb.getId());
            map.put("activityId", fb.getActivityId());
            map.put("userId", fb.getUserId());
            map.put("content", fb.getContent());
            map.put("createTime", fb.getCreateTime());

            ActivityInfo activity = activityInfoService.getOne(fb.getActivityId());
            if (activity != null) map.put("activityTitle", activity.getTitle());

            SysUser user = sysUserService.getOne(fb.getUserId());
            if (user != null) {
                map.put("userName", user.getUsername());
                map.put("realName", user.getRealName());
            }

            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(ActivityFeedback feedback) {
        feedback.setCreateTime(new java.util.Date());
        activityFeedbackService.save(feedback);
        return R.successMsg("提交心得体会成功");
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        activityFeedbackService.removeById(id);
        return R.successMsg("删除成功");
    }
}
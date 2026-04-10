package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.association.common.R;
import com.rabbiter.association.entity.ActivitySignIn;
import com.rabbiter.association.entity.SignInRecord;
import com.rabbiter.association.service.ActivitySignInService;
import com.rabbiter.association.service.SignInRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/activitySignIn")
public class ActivitySignInController {

    @Autowired
    private ActivitySignInService activitySignInService;

    @Autowired
    private SignInRecordService signInRecordService;

    /**
     * 社长发起签到
     * @param activityId 活动ID
     * @return
     */
    @PostMapping("/launch")
    public R launchSignIn(Long activityId) {
        // 判断是否已经发起了该活动的签到
        QueryWrapper<ActivitySignIn> qw = new QueryWrapper<>();
        qw.eq("activity_id", activityId).eq("sign_in_status", 1);
        if (activitySignInService.count(qw) > 0) {
            return R.warn("该活动已在签到中，请勿重复发起");
        }

        ActivitySignIn signIn = new ActivitySignIn();
        signIn.setActivityId(activityId);
        signIn.setSignInStatus(1); // 1-正在签到
        signIn.setLaunchTime(new Date());
        activitySignInService.save(signIn);

        // 返回签到记录的ID，可以供前端生成打卡二维码
        return R.successData(signIn.getSignInId());
    }

    /**
     * 结束签到
     */
    @PostMapping("/finish")
    public R finishSignIn(Long signInId) {
        ActivitySignIn signIn = activitySignInService.getById(signInId);
        if (signIn == null) return R.error("签到记录不存在");

        signIn.setSignInStatus(0); // 0-已结束
        activitySignInService.updateById(signIn);
        return R.successMsg("已结束签到");
    }

    /**
     * 社员扫码打卡 (往 sign_in_record 插入记录)
     * @param signInId 活动签到发起ID
     * @param userId 社员ID
     * @return
     */
    @PostMapping("/punchCard")
    public R punchCard(Long signInId, Long userId) {
        // 检查签到是否在进行中
        ActivitySignIn signIn = activitySignInService.getById(signInId);
        if (signIn == null) {
            return R.error("签到活动不存在");
        }
        if (signIn.getSignInStatus() != 1) {
            return R.warn("签到已结束或未开启");
        }

        // 检查是否已经签过到
        QueryWrapper<SignInRecord> recordQw = new QueryWrapper<>();
        recordQw.eq("sign_in_id", signInId).eq("user_id", userId);
        if (signInRecordService.count(recordQw) > 0) {
            return R.warn("您已签到，请勿重复打卡");
        }

        SignInRecord record = new SignInRecord();
        record.setSignInId(signInId);
        record.setUserId(userId);
        record.setStatus(1); // 1-正常签到
        record.setSignTime(new Date());
        signInRecordService.save(record);

        return R.successMsg("签到打卡成功");
    }
}

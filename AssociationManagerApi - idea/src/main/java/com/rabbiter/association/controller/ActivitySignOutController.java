package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.entity.ActivitySignOut;
import com.rabbiter.association.entity.SignOutRecord;
import com.rabbiter.association.service.ActivitySignOutService;
import com.rabbiter.association.service.SignOutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/activitySignOut")
public class ActivitySignOutController {

    @Autowired
    private ActivitySignOutService activitySignOutService;

    @Autowired
    private SignOutRecordService signOutRecordService;

    /**
     * 社长发起签退
     * @param activityId 活动ID
     * @return
     */
    @PostMapping("/launch")
    public R launchSignOut(Long activityId) {
        QueryWrapper<ActivitySignOut> qw = new QueryWrapper<>();
        qw.eq("activity_id", activityId).eq("sign_out_status", 1);
        if (activitySignOutService.count(qw) > 0) {
            return R.warn("该活动已在签退中，请勿重复发起");
        }

        ActivitySignOut signOut = new ActivitySignOut();
        signOut.setActivityId(activityId);
        signOut.setSignOutStatus(1); // 1-正在签退
        signOut.setLaunchTime(new Date());
        activitySignOutService.save(signOut);

        return R.successData(signOut.getId());
    }

    /**
     * 结束签退
     */
    @PostMapping("/finish")
    public R finishSignOut(Long signOutId) {
        ActivitySignOut signOut = activitySignOutService.getById(signOutId);
        if (signOut == null) return R.error("签退记录不存在");

        signOut.setSignOutStatus(0); // 0-已结束
        activitySignOutService.updateById(signOut);
        return R.successMsg("已结束签退");
    }

    /**
     * 社员扫码签退打卡
     * @param signOutId 签退发起ID
     * @param userId 社员ID
     * @return
     */
    @PostMapping("/punchCard")
    public R punchCard(Long signOutId, Long userId) {
        ActivitySignOut signOut = activitySignOutService.getById(signOutId);
        if (signOut == null) {
            return R.error("签退活动不存在");
        }
        if (signOut.getSignOutStatus() != 1) {
            return R.warn("签退已结束或未开启");
        }

        QueryWrapper<SignOutRecord> recordQw = new QueryWrapper<>();
        recordQw.eq("sign_out_id", signOutId).eq("user_id", userId);
        if (signOutRecordService.count(recordQw) > 0) {
            return R.warn("您已签退，请勿重复打卡");
        }

        SignOutRecord record = new SignOutRecord();
        record.setSignOutId(signOutId);
        record.setUserId(userId);
        record.setStatus(1); // 1-正常签退
        record.setSignTime(new Date());
        signOutRecordService.save(record);

        return R.successMsg("签退打卡成功");
    }
}

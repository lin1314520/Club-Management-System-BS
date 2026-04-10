package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("activity_sign_in")
public class ActivitySignIn {
    @TableId(value = "sign_in_id", type = IdType.AUTO)
    private Long id;
    @TableField("activity_id")
    private Long activityId;
    @TableField("sign_in_status")
    private Integer signInStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("launch_time")
    private Date launchTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Integer getSignInStatus() { return signInStatus; }
    public void setSignInStatus(Integer signInStatus) { this.signInStatus = signInStatus; }
    public Date getLaunchTime() { return launchTime; }
    public void setLaunchTime(Date launchTime) { this.launchTime = launchTime; }
}
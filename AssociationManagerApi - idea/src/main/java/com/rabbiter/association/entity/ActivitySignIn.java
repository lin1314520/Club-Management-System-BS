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
    @TableField("user_id")
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("sign_in_time")
    private Date signInTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Date getSignInTime() { return signInTime; }
    public void setSignInTime(Date signInTime) { this.signInTime = signInTime; }
}
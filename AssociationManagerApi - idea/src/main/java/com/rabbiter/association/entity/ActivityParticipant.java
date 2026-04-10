package com.rabbiter.association.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据实体类
 * 活动参与表
 */
@TableName("activity_application")
public class ActivityParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动申请id
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 参与用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 审核状态
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 申请理由
     */
    @TableField("apply_reason")
    private String applyReason;

    /**
     * 申请反馈
     */
    @TableField("feedback")
    private String feedback;

    /**
     * 报名时间
     */
    @TableField("apply_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
}
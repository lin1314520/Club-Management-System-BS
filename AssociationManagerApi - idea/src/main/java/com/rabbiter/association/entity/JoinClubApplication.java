package com.rabbiter.association.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 入社申请表
 */
@TableName("join_club_application")
public class JoinClubApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "join_app_id", type = IdType.AUTO)
    private Long joinAppId;

    @TableField("club_id")
    private Long clubId;

    @TableField("user_id")
    private Long userId;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("join_reason")
    private String joinReason;

    @TableField("apply_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    @TableField("audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @TableField("feedback")
    private String feedback;

    // Getters and Setters
    public Long getJoinAppId() { return joinAppId; }
    public void setJoinAppId(Long joinAppId) { this.joinAppId = joinAppId; }

    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }

    public String getJoinReason() { return joinReason; }
    public void setJoinReason(String joinReason) { this.joinReason = joinReason; }

    public Date getApplyTime() { return applyTime; }
    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }

    public Date getAuditTime() { return auditTime; }
    public void setAuditTime(Date auditTime) { this.auditTime = auditTime; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}

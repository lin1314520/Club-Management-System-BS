import os
import re

entity_dir = "src/main/java/com/rabbiter/association/entity"

def fix_table_id(file_name, id_col_name):
    path = os.path.join(entity_dir, file_name)
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    content = re.sub(r'@TableId\(.*?IdType\.AUTO\)', f'@TableId(value = "{id_col_name}", type = IdType.AUTO)', content)
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

fix_table_id("SysUser.java", "user_id")
fix_table_id("ClubType.java", "type_id")
fix_table_id("ClubInfo.java", "club_id")
fix_table_id("ClubApplication.java", "application_id")
fix_table_id("ClubMember.java", "member_id")
fix_table_id("ActivityInfo.java", "activity_id")
fix_table_id("ActivityFeedback.java", "feedback_id")
fix_table_id("NotificationInfo.java", "notification_id")
fix_table_id("PaymentNotice.java", "notice_id")
fix_table_id("PaymentRecord.java", "record_id")
fix_table_id("WithdrawalRecord.java", "record_id")

# Overwrite ActivityParticipant.java
ap_content = """package com.rabbiter.association.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "activity_participant")
public class ActivityParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "participant_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "activity_id")
    private Long activityId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "audit_status")
    private Integer auditStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "apply_time")
    private Date applyTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public Date getApplyTime() { return applyTime; }
    public void setApplyTime(Date applyTime) { this.applyTime = applyTime; }
}
"""
with open(os.path.join(entity_dir, "ActivityParticipant.java"), 'w', encoding='utf-8') as f:
    f.write(ap_content)


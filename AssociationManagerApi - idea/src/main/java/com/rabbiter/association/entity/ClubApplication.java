package com.rabbiter.association.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据实体类
 * 建社申请表
 */
@TableName(value = "club_application")
public class ClubApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "application_id", type = IdType.AUTO)
    private Long id;

    /**
     * 申请人ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 申请创建的社团名称
     */
    @TableField(value = "club_name")
    private String clubName;

    /**
     * 社团类型ID
     */
    @TableField(value = "type_id")
    private Long typeId;

    /**
     * 申请理由及简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 审批状态：0-审批中，1-已通过，2-已拒绝，3-已撤销
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 申请时间
     */
    @TableField(value = "apply_time")
    private Date applyTime;

    /**
     * 审批时间
     */
    @TableField(value = "audit_time")
    private Date auditTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
}
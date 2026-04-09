package com.rabbiter.association.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据实体类
 * 缴费记录表
 */
@TableName(value = "payment_record")
public class PaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long id;

    /**
     * 缴费通知ID
     */
    @TableField(value = "notice_id")
    private Long noticeId;

    /**
     * 缴费用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 实际缴费金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 状态：0-未缴费，1-已缴费
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 缴费时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
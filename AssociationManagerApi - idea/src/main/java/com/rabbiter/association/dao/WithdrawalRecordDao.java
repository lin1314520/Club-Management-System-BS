package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.association.entity.WithdrawalRecord;

@Repository
public interface WithdrawalRecordDao extends BaseMapper<WithdrawalRecord> {
}

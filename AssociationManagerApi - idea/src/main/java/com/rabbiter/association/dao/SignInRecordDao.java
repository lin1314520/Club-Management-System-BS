package com.rabbiter.association.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.association.entity.SignInRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignInRecordDao extends BaseMapper<SignInRecord> {
}

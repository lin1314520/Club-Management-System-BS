package com.rabbiter.association.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.association.entity.SignOutRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignOutRecordDao extends BaseMapper<SignOutRecord> {
}

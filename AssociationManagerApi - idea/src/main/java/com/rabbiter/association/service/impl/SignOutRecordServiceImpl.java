package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.SignOutRecordDao;
import com.rabbiter.association.entity.SignOutRecord;
import com.rabbiter.association.service.SignOutRecordService;
import org.springframework.stereotype.Service;

@Service
public class SignOutRecordServiceImpl extends ServiceImpl<SignOutRecordDao, SignOutRecord> implements SignOutRecordService {
}

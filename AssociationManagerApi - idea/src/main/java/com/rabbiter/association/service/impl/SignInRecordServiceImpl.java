package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.SignInRecordDao;
import com.rabbiter.association.entity.SignInRecord;
import com.rabbiter.association.service.SignInRecordService;
import org.springframework.stereotype.Service;

@Service
public class SignInRecordServiceImpl extends ServiceImpl<SignInRecordDao, SignInRecord> implements SignInRecordService {
}

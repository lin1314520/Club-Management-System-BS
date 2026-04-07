package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.ActivitySignInDao;
import com.rabbiter.association.entity.ActivitySignIn;
import com.rabbiter.association.service.ActivitySignInService;
import org.springframework.stereotype.Service;
@Service
public class ActivitySignInServiceImpl extends ServiceImpl<ActivitySignInDao, ActivitySignIn> implements ActivitySignInService {
}

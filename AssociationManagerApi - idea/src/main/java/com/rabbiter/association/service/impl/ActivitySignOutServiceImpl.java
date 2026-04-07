package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.ActivitySignOutDao;
import com.rabbiter.association.entity.ActivitySignOut;
import com.rabbiter.association.service.ActivitySignOutService;
import org.springframework.stereotype.Service;
@Service
public class ActivitySignOutServiceImpl extends ServiceImpl<ActivitySignOutDao, ActivitySignOut> implements ActivitySignOutService {
}

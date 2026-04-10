package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.JoinClubApplicationDao;
import com.rabbiter.association.entity.JoinClubApplication;
import com.rabbiter.association.service.JoinClubApplicationService;
import org.springframework.stereotype.Service;

@Service
public class JoinClubApplicationServiceImpl extends ServiceImpl<JoinClubApplicationDao, JoinClubApplication> implements JoinClubApplicationService {
}

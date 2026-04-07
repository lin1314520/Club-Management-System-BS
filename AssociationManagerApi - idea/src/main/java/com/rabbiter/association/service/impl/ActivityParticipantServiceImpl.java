package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ActivityParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ActivityParticipant;
import com.rabbiter.association.dao.ActivityParticipantDao;

@Service
public class ActivityParticipantServiceImpl extends ServiceImpl<ActivityParticipantDao, ActivityParticipant> implements ActivityParticipantService {

    @Autowired
    private ActivityParticipantDao activityParticipantDao;

    @Override
    @Transactional
    public void add(ActivityParticipant entity) {
        activityParticipantDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ActivityParticipant entity) {
        activityParticipantDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ActivityParticipant entity) {
        activityParticipantDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ActivityParticipant getOne(Long id) {
        return activityParticipantDao.selectById(id);
    }
}

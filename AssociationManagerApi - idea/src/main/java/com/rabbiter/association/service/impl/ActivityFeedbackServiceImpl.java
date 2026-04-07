package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ActivityFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ActivityFeedback;
import com.rabbiter.association.dao.ActivityFeedbackDao;

@Service
public class ActivityFeedbackServiceImpl extends ServiceImpl<ActivityFeedbackDao, ActivityFeedback> implements ActivityFeedbackService {

    @Autowired
    private ActivityFeedbackDao activityFeedbackDao;

    @Override
    @Transactional
    public void add(ActivityFeedback entity) {
        activityFeedbackDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ActivityFeedback entity) {
        activityFeedbackDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ActivityFeedback entity) {
        activityFeedbackDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ActivityFeedback getOne(Long id) {
        return activityFeedbackDao.selectById(id);
    }
}

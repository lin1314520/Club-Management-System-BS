package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ActivityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ActivityInfo;
import com.rabbiter.association.dao.ActivityInfoDao;

@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoDao, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoDao activityInfoDao;

    @Override
    @Transactional
    public void add(ActivityInfo entity) {
        activityInfoDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ActivityInfo entity) {
        activityInfoDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ActivityInfo entity) {
        activityInfoDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ActivityInfo getOne(Long id) {
        return activityInfoDao.selectById(id);
    }
}

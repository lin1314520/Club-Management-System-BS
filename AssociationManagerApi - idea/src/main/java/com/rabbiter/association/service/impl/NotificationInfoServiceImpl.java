package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.NotificationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.NotificationInfo;
import com.rabbiter.association.dao.NotificationInfoDao;

@Service
public class NotificationInfoServiceImpl extends ServiceImpl<NotificationInfoDao, NotificationInfo> implements NotificationInfoService {

    @Autowired
    private NotificationInfoDao notificationInfoDao;

    @Override
    @Transactional
    public void add(NotificationInfo entity) {
        notificationInfoDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(NotificationInfo entity) {
        notificationInfoDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(NotificationInfo entity) {
        notificationInfoDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public NotificationInfo getOne(Long id) {
        return notificationInfoDao.selectById(id);
    }
}

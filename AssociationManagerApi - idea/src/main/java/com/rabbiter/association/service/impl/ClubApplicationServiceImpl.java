package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ClubApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ClubApplication;
import com.rabbiter.association.dao.ClubApplicationDao;

@Service
public class ClubApplicationServiceImpl extends ServiceImpl<ClubApplicationDao, ClubApplication> implements ClubApplicationService {

    @Autowired
    private ClubApplicationDao clubApplicationDao;

    @Override
    @Transactional
    public void add(ClubApplication entity) {
        clubApplicationDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ClubApplication entity) {
        clubApplicationDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ClubApplication entity) {
        clubApplicationDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ClubApplication getOne(Long id) {
        return clubApplicationDao.selectById(id);
    }
}

package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ClubInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ClubInfo;
import com.rabbiter.association.dao.ClubInfoDao;

@Service
public class ClubInfoServiceImpl extends ServiceImpl<ClubInfoDao, ClubInfo> implements ClubInfoService {

    @Autowired
    private ClubInfoDao clubInfoDao;

    @Override
    @Transactional
    public void add(ClubInfo entity) {
        clubInfoDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ClubInfo entity) {
        clubInfoDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ClubInfo entity) {
        clubInfoDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ClubInfo getOne(Long id) {
        return clubInfoDao.selectById(id);
    }
}

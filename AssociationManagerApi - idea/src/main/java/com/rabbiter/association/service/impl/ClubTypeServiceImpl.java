package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ClubTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ClubType;
import com.rabbiter.association.dao.ClubTypeDao;

@Service
public class ClubTypeServiceImpl extends ServiceImpl<ClubTypeDao, ClubType> implements ClubTypeService {

    @Autowired
    private ClubTypeDao clubTypeDao;

    @Override
    @Transactional
    public void add(ClubType entity) {
        clubTypeDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ClubType entity) {
        clubTypeDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ClubType entity) {
        clubTypeDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ClubType getOne(Long id) {
        return clubTypeDao.selectById(id);
    }
}

package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.dao.SysUserDao;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    @Transactional
    public void add(SysUser entity) {
        sysUserDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(SysUser entity) {
        sysUserDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(SysUser entity) {
        sysUserDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SysUser getOne(Long id) {
        return sysUserDao.selectById(id);
    }
}

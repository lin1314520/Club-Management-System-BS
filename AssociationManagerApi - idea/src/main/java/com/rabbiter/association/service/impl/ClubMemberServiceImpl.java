package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.ClubMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.ClubMember;
import com.rabbiter.association.dao.ClubMemberDao;

@Service
public class ClubMemberServiceImpl extends ServiceImpl<ClubMemberDao, ClubMember> implements ClubMemberService {

    @Autowired
    private ClubMemberDao clubMemberDao;

    @Override
    @Transactional
    public void add(ClubMember entity) {
        clubMemberDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(ClubMember entity) {
        clubMemberDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(ClubMember entity) {
        clubMemberDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ClubMember getOne(Long id) {
        return clubMemberDao.selectById(id);
    }
}

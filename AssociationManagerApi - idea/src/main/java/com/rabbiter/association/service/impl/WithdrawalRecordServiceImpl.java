package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.WithdrawalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.WithdrawalRecord;
import com.rabbiter.association.dao.WithdrawalRecordDao;

@Service
public class WithdrawalRecordServiceImpl extends ServiceImpl<WithdrawalRecordDao, WithdrawalRecord> implements WithdrawalRecordService {

    @Autowired
    private WithdrawalRecordDao withdrawalRecordDao;

    @Override
    @Transactional
    public void add(WithdrawalRecord entity) {
        withdrawalRecordDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(WithdrawalRecord entity) {
        withdrawalRecordDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(WithdrawalRecord entity) {
        withdrawalRecordDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public WithdrawalRecord getOne(Long id) {
        return withdrawalRecordDao.selectById(id);
    }
}

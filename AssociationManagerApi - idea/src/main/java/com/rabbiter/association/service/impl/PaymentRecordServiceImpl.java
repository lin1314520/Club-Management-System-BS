package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.PaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.PaymentRecord;
import com.rabbiter.association.dao.PaymentRecordDao;

@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordDao, PaymentRecord> implements PaymentRecordService {

    @Autowired
    private PaymentRecordDao paymentRecordDao;

    @Override
    @Transactional
    public void add(PaymentRecord entity) {
        paymentRecordDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(PaymentRecord entity) {
        paymentRecordDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(PaymentRecord entity) {
        paymentRecordDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PaymentRecord getOne(Long id) {
        return paymentRecordDao.selectById(id);
    }
}

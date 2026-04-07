package com.rabbiter.association.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.service.PaymentNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.rabbiter.association.entity.PaymentNotice;
import com.rabbiter.association.dao.PaymentNoticeDao;

@Service
public class PaymentNoticeServiceImpl extends ServiceImpl<PaymentNoticeDao, PaymentNotice> implements PaymentNoticeService {

    @Autowired
    private PaymentNoticeDao paymentNoticeDao;

    @Override
    @Transactional
    public void add(PaymentNotice entity) {
        paymentNoticeDao.insert(entity);
    }

    @Override
    @Transactional
    public void update(PaymentNotice entity) {
        paymentNoticeDao.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(PaymentNotice entity) {
        paymentNoticeDao.deleteById(entity.getId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PaymentNotice getOne(Long id) {
        return paymentNoticeDao.selectById(id);
    }
}

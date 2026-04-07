package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.SysPresidentDao;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.service.SysPresidentService;
import org.springframework.stereotype.Service;
@Service
public class SysPresidentServiceImpl extends ServiceImpl<SysPresidentDao, SysPresident> implements SysPresidentService {
}

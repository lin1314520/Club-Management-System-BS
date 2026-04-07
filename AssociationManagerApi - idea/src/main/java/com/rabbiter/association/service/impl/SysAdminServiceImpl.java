package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.SysAdminDao;
import com.rabbiter.association.entity.SysAdmin;
import com.rabbiter.association.service.SysAdminService;
import org.springframework.stereotype.Service;
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminDao, SysAdmin> implements SysAdminService {
}

package com.rabbiter.association.dao;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.association.entity.SysUser;

@Repository
public interface SysUserDao extends BaseMapper<SysUser> {
}

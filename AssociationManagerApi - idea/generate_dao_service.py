import os

base_dir = "src/main/java/com/rabbiter/association"

# 1. Delete old DAOs and Services
to_delete = [
    "dao/SysRoleDao.java", "dao/SysUserRoleDao.java",
    "service/SysRoleService.java", "service/SysUserRoleService.java",
    "service/impl/SysRoleServiceImpl.java", "service/impl/SysUserRoleServiceImpl.java"
]
for f in to_delete:
    p = os.path.join(base_dir, f)
    if os.path.exists(p):
        os.remove(p)

# 2. Create new DAOs and Services
new_entities = ["SysAdmin", "SysPresident", "ActivitySignIn", "ActivitySignOut", "FeedbackReply", "ActivityTweet"]

for entity in new_entities:
    # DAO
    dao_content = f"""package com.rabbiter.association.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.association.entity.{entity};
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface {entity}Dao extends BaseMapper<{entity}> {{
}}
"""
    with open(os.path.join(base_dir, f"dao/{entity}Dao.java"), 'w', encoding='utf-8') as f:
        f.write(dao_content)

    # Service
    var_name = entity[0].lower() + entity[1:]
    service_content = f"""package com.rabbiter.association.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rabbiter.association.entity.{entity};
public interface {entity}Service extends IService<{entity}> {{
}}
"""
    with open(os.path.join(base_dir, f"service/{entity}Service.java"), 'w', encoding='utf-8') as f:
        f.write(service_content)

    # ServiceImpl
    impl_content = f"""package com.rabbiter.association.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.association.dao.{entity}Dao;
import com.rabbiter.association.entity.{entity};
import com.rabbiter.association.service.{entity}Service;
import org.springframework.stereotype.Service;
@Service
public class {entity}ServiceImpl extends ServiceImpl<{entity}Dao, {entity}> implements {entity}Service {{
}}
"""
    with open(os.path.join(base_dir, f"service/impl/{entity}ServiceImpl.java"), 'w', encoding='utf-8') as f:
        f.write(impl_content)


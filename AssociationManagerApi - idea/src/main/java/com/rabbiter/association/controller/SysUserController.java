package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.association.entity.SysAdmin;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.SysAdminService;
import com.rabbiter.association.service.SysPresidentService;
import com.rabbiter.association.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysAdminService sysAdminService;

    @Autowired
    private SysPresidentService sysPresidentService;

    @GetMapping("/info")
    public R getInfo(Long id, Integer type) {
        if (type != null) {
            if (type == 0) return R.successData(sysAdminService.getById(id));
            if (type == 1) return R.successData(sysPresidentService.getById(id));
        }
        SysUser user = sysUserService.getById(id);
        return R.successData(user);
    }

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, String username, String realName, String phone) {
        List<Map<String, Object>> allUsers = new ArrayList<>();

        // 查询普通用户 (type = 2)
        QueryWrapper<SysUser> qwUser = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) qwUser.like("username", username);
        if (!StringUtils.isEmpty(realName)) qwUser.like("real_name", realName);
        if (!StringUtils.isEmpty(phone)) qwUser.like("phone", phone);
        List<SysUser> users = sysUserService.list(qwUser);
        for (SysUser u : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("realName", u.getRealName());
            map.put("phone", u.getPhone());
            map.put("email", u.getEmail());
            map.put("status", u.getStatus());
            map.put("createTime", u.getCreateTime());
            map.put("type", 2);
            allUsers.add(map);
        }

        // 查询管理员 (type = 0)
        QueryWrapper<SysAdmin> qwAdmin = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) qwAdmin.like("username", username);
        if (!StringUtils.isEmpty(realName)) qwAdmin.like("real_name", realName);
        if (!StringUtils.isEmpty(phone)) qwAdmin.like("phone", phone);
        List<SysAdmin> admins = sysAdminService.list(qwAdmin);
        for (SysAdmin a : admins) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("username", a.getUsername());
            map.put("realName", a.getRealName());
            map.put("phone", a.getPhone());
            map.put("email", a.getEmail());
            map.put("status", a.getStatus());
            map.put("createTime", a.getCreateTime());
            map.put("type", 0);
            allUsers.add(map);
        }

        // 查询社长 (type = 1)
        QueryWrapper<SysPresident> qwPresident = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) qwPresident.like("username", username);
        if (!StringUtils.isEmpty(realName)) qwPresident.like("real_name", realName);
        if (!StringUtils.isEmpty(phone)) qwPresident.like("phone", phone);
        List<SysPresident> presidents = sysPresidentService.list(qwPresident);
        for (SysPresident p : presidents) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("username", p.getUsername());
            map.put("realName", p.getRealName());
            map.put("phone", p.getPhone());
            map.put("email", p.getEmail());
            map.put("status", p.getStatus());
            map.put("createTime", p.getCreateTime());
            map.put("type", 1);
            allUsers.add(map);
        }

        // 按创建时间倒序排序
        allUsers.sort((o1, o2) -> {
            Date d1 = (Date) o1.get("createTime");
            Date d2 = (Date) o2.get("createTime");
            if (d1 == null || d2 == null) return 0;
            return d2.compareTo(d1);
        });

        // 内存分页
        int total = allUsers.size();
        int fromIndex = (int) ((pageIndex - 1) * pageSize);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        List<Map<String, Object>> pageList = new ArrayList<>();
        if (fromIndex < total) {
            pageList = allUsers.subList(fromIndex, toIndex);
        }

        PageData pageData = new PageData(pageIndex, pageSize, (long) total, pageList);
        return R.successData(pageData);
    }

    @PostMapping("/add")
    public R addInfo(SysUser sysUser, Integer type) {
        if (type == null) type = 2; // 默认普通用户

        if (type == 0) {
            QueryWrapper<SysAdmin> qw = new QueryWrapper<>();
            qw.eq("username", sysUser.getUsername());
            if (sysAdminService.count(qw) > 0) return R.warn("用户名已存在");
            SysAdmin admin = new SysAdmin();
            admin.setUsername(sysUser.getUsername());
            admin.setPassword(sysUser.getPassword());
            admin.setRealName(sysUser.getRealName());
            admin.setPhone(sysUser.getPhone());
            admin.setEmail(sysUser.getEmail());
            admin.setStatus(sysUser.getStatus() != null ? sysUser.getStatus() : 1);
            admin.setCreateTime(new Date());
            sysAdminService.save(admin);
        } else if (type == 1) {
            QueryWrapper<SysPresident> qw = new QueryWrapper<>();
            qw.eq("username", sysUser.getUsername());
            if (sysPresidentService.count(qw) > 0) return R.warn("用户名已存在");
            SysPresident president = new SysPresident();
            president.setUsername(sysUser.getUsername());
            president.setPassword(sysUser.getPassword());
            president.setRealName(sysUser.getRealName());
            president.setPhone(sysUser.getPhone());
            president.setEmail(sysUser.getEmail());
            president.setStatus(sysUser.getStatus() != null ? sysUser.getStatus() : 1);
            president.setCreateTime(new Date());
            sysPresidentService.save(president);
        } else {
            QueryWrapper<SysUser> qw = new QueryWrapper<>();
            qw.eq("username", sysUser.getUsername());
            if (sysUserService.count(qw) > 0) return R.warn("用户名已存在");
            sysUser.setCreateTime(new Date());
            if (sysUser.getStatus() == null) sysUser.setStatus(1);
            sysUser.setMemberStatus(0);
            sysUserService.save(sysUser);
        }
        return R.success();
    }

    @PostMapping("/upd")
    public R updInfo(SysUser sysUser, Integer type) {
        if (type == null) type = 2;
        if (type == 0) {
            SysAdmin admin = sysAdminService.getById(sysUser.getId());
            if (admin != null) {
                if (!StringUtils.isEmpty(sysUser.getPassword())) admin.setPassword(sysUser.getPassword());
                admin.setRealName(sysUser.getRealName());
                admin.setPhone(sysUser.getPhone());
                admin.setEmail(sysUser.getEmail());
                admin.setStatus(sysUser.getStatus());
                admin.setUpdateTime(new Date());
                sysAdminService.updateById(admin);
            }
        } else if (type == 1) {
            SysPresident president = sysPresidentService.getById(sysUser.getId());
            if (president != null) {
                if (!StringUtils.isEmpty(sysUser.getPassword())) president.setPassword(sysUser.getPassword());
                president.setRealName(sysUser.getRealName());
                president.setPhone(sysUser.getPhone());
                president.setEmail(sysUser.getEmail());
                president.setStatus(sysUser.getStatus());
                president.setUpdateTime(new Date());
                sysPresidentService.updateById(president);
            }
        } else {
            SysUser user = sysUserService.getById(sysUser.getId());
            if (user != null) {
                if (!StringUtils.isEmpty(sysUser.getPassword())) user.setPassword(sysUser.getPassword());
                user.setRealName(sysUser.getRealName());
                user.setPhone(sysUser.getPhone());
                user.setEmail(sysUser.getEmail());
                user.setStatus(sysUser.getStatus());
                user.setUpdateTime(new Date());
                sysUserService.updateById(user);
            }
        }
        return R.success();
    }

    @PostMapping("/del")
    public R delInfo(Long id, Integer type) {
        if (type == null) type = 2;
        if (type == 0) {
            sysAdminService.removeById(id);
        } else if (type == 1) {
            sysPresidentService.removeById(id);
        } else {
            sysUserService.removeById(id);
        }
        return R.success();
    }

    @PostMapping("/changeRole")
    public R changeRole(Long id, Integer currentType, Integer targetType) {
        if (currentType.equals(targetType)) return R.success();
        
        SysUser commonUser = null;
        
        // 提取原用户信息并删除原记录
        if (currentType == 0) {
            SysAdmin admin = sysAdminService.getById(id);
            if (admin == null) return R.error("用户不存在");
            commonUser = new SysUser();
            commonUser.setUsername(admin.getUsername());
            commonUser.setPassword(admin.getPassword());
            commonUser.setRealName(admin.getRealName());
            commonUser.setPhone(admin.getPhone());
            commonUser.setEmail(admin.getEmail());
            commonUser.setStatus(admin.getStatus());
            sysAdminService.removeById(id);
        } else if (currentType == 1) {
            SysPresident president = sysPresidentService.getById(id);
            if (president == null) return R.error("用户不存在");
            commonUser = new SysUser();
            commonUser.setUsername(president.getUsername());
            commonUser.setPassword(president.getPassword());
            commonUser.setRealName(president.getRealName());
            commonUser.setPhone(president.getPhone());
            commonUser.setEmail(president.getEmail());
            commonUser.setStatus(president.getStatus());
            sysPresidentService.removeById(id);
        } else {
            commonUser = sysUserService.getById(id);
            if (commonUser == null) return R.error("用户不存在");
            sysUserService.removeById(id);
        }

        // 插入新记录
        if (targetType == 0) {
            SysAdmin admin = new SysAdmin();
            admin.setUsername(commonUser.getUsername());
            admin.setPassword(commonUser.getPassword());
            admin.setRealName(commonUser.getRealName());
            admin.setPhone(commonUser.getPhone());
            admin.setEmail(commonUser.getEmail());
            admin.setStatus(commonUser.getStatus());
            admin.setCreateTime(new Date());
            sysAdminService.save(admin);
        } else if (targetType == 1) {
            SysPresident president = new SysPresident();
            president.setUsername(commonUser.getUsername());
            president.setPassword(commonUser.getPassword());
            president.setRealName(commonUser.getRealName());
            president.setPhone(commonUser.getPhone());
            president.setEmail(commonUser.getEmail());
            president.setStatus(commonUser.getStatus());
            president.setCreateTime(new Date());
            sysPresidentService.save(president);
        } else {
            SysUser user = new SysUser();
            user.setUsername(commonUser.getUsername());
            user.setPassword(commonUser.getPassword());
            user.setRealName(commonUser.getRealName());
            user.setPhone(commonUser.getPhone());
            user.setEmail(commonUser.getEmail());
            user.setStatus(commonUser.getStatus());
            user.setCreateTime(new Date());
            user.setMemberStatus(0);
            sysUserService.save(user);
        }

        return R.successMsg("角色转换成功");
    }
}

package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.association.entity.SysUser;
import com.rabbiter.association.entity.SysPresident;
import com.rabbiter.association.entity.SysAdmin;
import com.rabbiter.association.entity.NotificationInfo;
import com.rabbiter.association.handle.CacheHandle;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.SysUserService;
import com.rabbiter.association.service.SysPresidentService;
import com.rabbiter.association.service.SysAdminService;
import com.rabbiter.association.service.NotificationInfoService;
import com.rabbiter.association.utils.IDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    private static final Logger Log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPresidentService sysPresidentService;

    @Autowired
    private SysAdminService sysAdminService;

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private NotificationInfoService notificationInfoService;

    @GetMapping("/sys/notices")
    @ResponseBody
    public R getNoticeList(String token){
        String userCache = cacheHandle.getUserInfoCache(token);
        if(ObjectUtils.isEmpty(userCache)) {
            return R.error("登录信息不存在，请重新登录");
        }

        QueryWrapper<NotificationInfo> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        List<NotificationInfo> list = notificationInfoService.list(qw);

        return R.successData(list);
    }

    @PostMapping("/login")
    @ResponseBody
    public R login(String userName, String passWord, HttpSession session){      
        Log.info("用户登录，用户名：{}， 密码：{}", userName, passWord);

        // 1. Check Admin
        QueryWrapper<SysAdmin> adminQw = new QueryWrapper<>();
        adminQw.eq("username", userName);
        SysAdmin admin = sysAdminService.getOne(adminQw);
        if (admin != null) {
            if (passWord.equals(admin.getPassword().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, "0_" + admin.getId());  
                return R.success("登录成功", token);
            } else return R.error("密码错误");
        }

        // 2. Check President
        QueryWrapper<SysPresident> presQw = new QueryWrapper<>();
        presQw.eq("username", userName);
        SysPresident president = sysPresidentService.getOne(presQw);
        if (president != null) {
            if (passWord.equals(president.getPassword().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, "1_" + president.getId());  
                return R.success("登录成功", token);
            } else return R.error("密码错误");
        }

        // 3. Check User
        QueryWrapper<SysUser> userQw = new QueryWrapper<>();
        userQw.eq("username", userName);
        SysUser user = sysUserService.getOne(userQw);
        if (user != null) {
            if (passWord.equals(user.getPassword().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, "2_" + user.getId());  
                return R.success("登录成功", token);
            } else return R.error("密码错误");
        }

        return R.error("输入的用户名不存在");
    }

    @RequestMapping("/exit")
    @ResponseBody
    public R exit(String token) {
        Log.info("用户退出系统并移除登录信息");
        cacheHandle.removeUserCache(token);
        return R.success();
    }

    @GetMapping("/info")
    @ResponseBody
    public R info(String token){
        String userCache = cacheHandle.getUserInfoCache(token);
        if(ObjectUtils.isEmpty(userCache)) {
            return R.error("登录信息不存在，请重新登录");
        }
        
        String[] parts = userCache.split("_");
        int type = Integer.parseInt(parts[0]);
        Long id = Long.valueOf(parts[1]);

        Map<String, Object> res = new HashMap<>();
        res.put("type", type);
        res.put("id", id);

        if (type == 0) {
            SysAdmin admin = sysAdminService.getById(id);
            res.put("userName", admin.getUsername());
            res.put("passWord", admin.getPassword());
            res.put("name", admin.getRealName());
            res.put("phone", admin.getPhone());
            res.put("email", admin.getEmail());
            res.put("status", admin.getStatus());
            res.put("createTime", admin.getCreateTime());
        } else if (type == 1) {
            SysPresident pres = sysPresidentService.getById(id);
            res.put("userName", pres.getUsername());
            res.put("passWord", pres.getPassword());
            res.put("name", pres.getRealName());
            res.put("phone", pres.getPhone());
            res.put("email", pres.getEmail());
            res.put("status", pres.getStatus());
            res.put("createTime", pres.getCreateTime());
        } else {
            SysUser user = sysUserService.getById(id);
            res.put("userName", user.getUsername());
            res.put("passWord", user.getPassword());
            res.put("name", user.getRealName());
            res.put("phone", user.getPhone());
            res.put("email", user.getEmail());
            res.put("status", user.getStatus());
            res.put("createTime", user.getCreateTime());
            res.put("memberStatus", user.getMemberStatus());
        }

        return R.successData(res);
    }

    @PostMapping("/info")
    @ResponseBody
    public R info(SysUser user){
        Log.info("修改用户信息（暂不支持多表通用修改），{}", user);
        // Fallback for user modification, simplified
        sysUserService.updateById(user);
        return R.success();
    }

    @RequestMapping("/checkPwd")
    @ResponseBody
    public R checkPwd(String oldPwd, String token) {
        String userCache = cacheHandle.getUserInfoCache(token);
        if(ObjectUtils.isEmpty(userCache)) return R.error("未登录");
        
        String[] parts = userCache.split("_");
        int type = Integer.parseInt(parts[0]);
        Long id = Long.valueOf(parts[1]);
        
        String currentPwd = "";
        if (type == 0) currentPwd = sysAdminService.getById(id).getPassword();
        else if (type == 1) currentPwd = sysPresidentService.getById(id).getPassword();
        else currentPwd = sysUserService.getById(id).getPassword();

        if(oldPwd.equals(currentPwd)) {
            return R.success();
        }else {
            return R.warn("原始密码和输入密码不一致");
        }
    }

    @PostMapping("/pwd")
    @ResponseBody
    public R pwd(String token, String password) {
        Log.info("修改用户密码，{}", password);
        String userCache = cacheHandle.getUserInfoCache(token);
        if(ObjectUtils.isEmpty(userCache)) return R.error("未登录");
        
        String[] parts = userCache.split("_");
        int type = Integer.parseInt(parts[0]);
        Long id = Long.valueOf(parts[1]);

        if (type == 0) {
            SysAdmin admin = sysAdminService.getById(id);
            admin.setPassword(password);
            sysAdminService.updateById(admin);
        } else if (type == 1) {
            SysPresident pres = sysPresidentService.getById(id);
            pres.setPassword(password);
            sysPresidentService.updateById(pres);
        } else {
            SysUser user = sysUserService.getById(id);
            user.setPassword(password);
            sysUserService.updateById(user);
        }

        return R.success();
    }
}

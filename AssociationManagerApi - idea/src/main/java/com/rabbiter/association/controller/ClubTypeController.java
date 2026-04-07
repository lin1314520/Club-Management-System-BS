package com.rabbiter.association.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbiter.association.entity.ClubType;
import com.rabbiter.association.msg.PageData;
import com.rabbiter.association.msg.R;
import com.rabbiter.association.service.ClubTypeService;
import com.rabbiter.association.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubType")
public class ClubTypeController extends BaseController {

    @Autowired
    private ClubTypeService clubTypeService;

    @GetMapping("/info")
    public R getInfo(Long id) {
        return R.successData(clubTypeService.getOne(id));
    }

    @GetMapping("/page")
    public R getPageInfos(Long pageIndex, Long pageSize, ClubType clubType) {
        QueryWrapper<ClubType> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clubType.getTypeName())) {
            qw.like("type_name", clubType.getTypeName());
        }
        qw.orderByDesc("create_time");

        Page<ClubType> page = clubTypeService.page(new Page<>(pageIndex, pageSize), qw);
        
        List<Map<String, Object>> resList = new ArrayList<>();
        for (ClubType type : page.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", type.getId());
            map.put("typeName", type.getTypeName());
            map.put("description", type.getDescription());
            map.put("createTime", type.getCreateTime());
            resList.add(map);
        }
        
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), resList);
        return R.successData(pageData);
    }

    @GetMapping("/list")
    public R getList() {
        return R.successData(clubTypeService.list());
    }

    @PostMapping("/add")
    public R addInfo(ClubType clubType) {
        clubType.setCreateTime(new java.util.Date());
        clubTypeService.save(clubType);
        return R.success();
    }

    @PostMapping("/upd")
    public R updInfo(ClubType clubType) {
        clubTypeService.updateById(clubType);
        return R.success();
    }

    @PostMapping("/del")
    public R delInfo(Long id) {
        clubTypeService.removeById(id);
        return R.success();
    }
}
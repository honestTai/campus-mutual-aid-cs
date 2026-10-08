package com.confession.back.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.Collect;
import com.confession.back.api.service.CollectService;
import com.confession.back.config.filter.isLogin;
import com.confession.back.util.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * 
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    CollectService collectService;

    @PostMapping("/add")
    @isLogin
    @SystemLog(info = "收藏")
    public void addCollect(@RequestBody Collect collect) throws ResultException {
        Integer count = collectService.count(new QueryWrapper<Collect>().eq("pkId", collect.getPkId()).eq("userId", ThreadLocal.getThreadLocal().getId()));
        if (count>0) {
            throw new ResultException(ResultStatus.COLLECT_AGAIN);
        } else {
            collect.setUserId(ThreadLocal.getThreadLocal().getId());
            collect.setDate(new Date());
            collectService.save(collect);
        }
    }

    @PostMapping("/del")
    @isLogin
    @SystemLog(info = "收藏信息删除")
    public void del(@RequestBody Collect collect){
        collectService.removeById(collect.getId());
    }
}


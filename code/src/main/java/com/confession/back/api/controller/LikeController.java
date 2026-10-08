package com.confession.back.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.Collect;
import com.confession.back.api.entity.Like;
import com.confession.back.api.service.LikeService;
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
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeService likeService;


    @PostMapping("/add")
    @isLogin
    @SystemLog(info = "点赞")
    public void addCollect(@RequestBody Like like) throws ResultException {
        Integer count = likeService.count(new QueryWrapper<Like>().eq("pkId", like.getPkId()).eq("userId", ThreadLocal.getThreadLocal().getId()));
        if (count > 0) {
            throw new ResultException(ResultStatus.LIKE_AGAIN);
        } else {
            like.setUserId(ThreadLocal.getThreadLocal().getId());
            like.setTime(new Date());
            likeService.save(like);
        }
    }

    @PostMapping("/del")
    @isLogin
    @SystemLog(info = "点赞取消")
    public void del(@RequestBody Collect collect) {
        likeService.removeById(collect.getId());
    }
}


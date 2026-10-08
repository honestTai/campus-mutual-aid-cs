package com.confession.back.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.confession.back.api.bean.vto.SearchVto;
import com.confession.back.api.entity.Collect;
import com.confession.back.api.entity.Comment;
import com.confession.back.api.entity.Confess;
import com.confession.back.api.entity.Like;
import com.confession.back.api.service.CollectService;
import com.confession.back.api.service.CommentService;
import com.confession.back.api.service.ConfessService;
import com.confession.back.api.service.LikeService;
import com.confession.back.config.filter.isLogin;
import com.confession.back.util.CheckInfo;
import com.confession.back.util.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * 
 */
@RestController
@RequestMapping("/confess")
public class ConfessController {

    @Autowired
    ConfessService confessService;

    @Autowired
    LikeService likeService;

    @Autowired
    CollectService collectService;

    @Autowired
    CommentService commentService;

    //发布内容
    @PostMapping("/creatConfess")
    @isLogin
    @CheckInfo
    @SystemLog(info = "发布内容")
    public void creatConfess(@RequestBody Confess confess) {
        confessService.creatConfess(confess);
    }

    //获取所有表白内容
    @PostMapping("/getList")
    public Page<Confess> confessPage(@RequestBody SearchVto searchVto) throws ParseException {
        return confessService.selectConfessByPage(searchVto);
    }

    @PostMapping("/getById")
    public Confess getById(@RequestBody SearchVto searchVto) throws ParseException {
        return confessService.getByIds(searchVto);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/del")
    @isLogin
    @SystemLog(info = "删除发布内容")
    public void del(@RequestBody Confess confess) {
        Integer id = confess.getId();
        confessService.removeById(id);
        collectService.remove(new QueryWrapper<Collect>().eq("pkId", confess.getId()));
        commentService.remove(new QueryWrapper<Comment>().eq("pkId", confess.getId()));
        likeService.remove(new QueryWrapper<Like>().eq("pkId", confess.getId()));
    }

}


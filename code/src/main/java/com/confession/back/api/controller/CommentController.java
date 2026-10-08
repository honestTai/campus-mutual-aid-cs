package com.confession.back.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.Collect;
import com.confession.back.api.entity.Comment;
import com.confession.back.api.service.CommentService;
import com.confession.back.api.service.UserService;
import com.confession.back.config.filter.isLogin;
import com.confession.back.util.CheckInfo;
import com.confession.back.util.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * 
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @PostMapping("/getAll")
    public List<Comment> commentList(@RequestBody Comment comment) {
        List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().eq("pkId", comment.getPkId()));
        if (commentList.size() > 0) {
            commentList.forEach(commentInfo -> {
                commentInfo.setUser(userService.getById(commentInfo.getUserId()));
            });
        }
        return commentList;
    }

    @PostMapping("/add")
    @isLogin
    @SystemLog(info = "评论")
    @CheckInfo
    public void addComment(@RequestBody Comment comment) {
        if (comment.getId() != null) {
            String reply=comment.getReply();
            comment = commentService.getById(comment.getId());
            comment.setReplyTime(new Date());
            comment.setReply(reply);
        } else {
            comment.setUserId(ThreadLocal.getThreadLocal().getId());
            comment.setTime(new Date());
        }
        commentService.saveOrUpdate(comment);
    }

    @PostMapping("/del")
    @isLogin
    @SystemLog(info = "评论内容删除")
    public void del(@RequestBody Collect collect){
        commentService.removeById(collect.getId());
    }

}


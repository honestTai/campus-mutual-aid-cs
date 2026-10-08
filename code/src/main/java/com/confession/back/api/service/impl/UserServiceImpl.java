package com.confession.back.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.dto.MineDto;
import com.confession.back.api.bean.dto.UserLoginDto;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.*;
import com.confession.back.api.mapper.*;
import com.confession.back.api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.confession.back.util.encryption.MD5Util.getMD5;
import static com.confession.back.util.jwt.JwtUtil.sign;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * 
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ConfessMapper confessMapper;

    @Autowired
    LikeMapper likeMapper;


    @Override
    public UserLoginDto login(User user) throws ResultException {
        User userInfo = userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber()).eq("password", getMD5(user.getPassword())));
        if (userInfo == null) {
            throw new ResultException(ResultStatus.ERROR_NUM_PWD);
        } else {
            return new UserLoginDto(userInfo, sign(userInfo.getNumber(), userInfo.getPassword()));
        }
    }

    /**
     * 我的收藏
     * 我的评论
     * 我的发布
     *
     * @return
     */
    @Override
    public MineDto mineDtoList() {
        Integer userId = ThreadLocal.getThreadLocal().getId();
        List<Collect> collects = collectMapper.selectList(new QueryWrapper<Collect>().eq("userId", userId));
        if(collects.size()>0){
            collects.forEach(collect -> {
                collect.setConfess(confessMapper.selectById(collect.getPkId()));
            });
        }
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("userId", userId));
        if(commentList.size()>0){
            commentList.forEach(comment -> {
                comment.setConfess(confessMapper.selectById(comment.getPkId()));
            });
        }
        List<Confess> confesses = confessMapper.selectList(new QueryWrapper<Confess>().eq("userId", userId));
        List<Like> likes = likeMapper.selectList(new QueryWrapper<Like>().eq("userId", userId));
        if(likes.size()>0){
            likes.forEach(like -> {
                like.setConfess(confessMapper.selectById(like.getPkId()));
            });
        }
        return new MineDto(collects, confesses, commentList,likes);
    }
}

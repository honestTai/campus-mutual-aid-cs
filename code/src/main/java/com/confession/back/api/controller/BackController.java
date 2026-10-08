package com.confession.back.api.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.confession.back.api.bean.dto.UserLoginDto;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.bean.vto.SearchVto;
import com.confession.back.api.entity.*;
import com.confession.back.api.mapper.*;
import com.confession.back.api.service.*;
import com.confession.back.config.filter.isLogin;
import com.confession.back.util.BackUse;
import com.confession.back.util.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.confession.back.util.encryption.MD5Util.getMD5;
import static com.confession.back.util.jwt.JwtUtil.sign;

@RestController
@RequestMapping("/back")
public class BackController {

    @Autowired
    AdminService adminService;

    @Autowired
    ConfessService confessService;

    @Autowired
    CommentService commentService;

    @Autowired
    CollectService collectService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LikeMapper likeMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CollectMapper collectMapper;

    @PostMapping("/login")
    public UserLoginDto userLoginDto(@RequestBody Admin admin) throws ResultException {
        Admin adminInfo = adminService.getOne(new QueryWrapper<Admin>().eq("number", admin.getNumber()).eq("password", getMD5(admin.getPassword())));
        if (adminInfo == null) {
            throw new ResultException(ResultStatus.ERROR_NUM_PWD);
        } else {
            return new UserLoginDto(adminInfo, sign(adminInfo.getNumber(), adminInfo.getPassword()));
        }
    }


    @PostMapping("/list")
    @isLogin
    @BackUse
    @SystemLog(info = "后台获取所有发布内容")
    public Page<Confess> list(@RequestBody SearchVto searchVto) throws ResultException {
        QueryWrapper<Confess> queryWrapper = new QueryWrapper<>();
        if (!searchVto.getLikeString().isEmpty()) {
            queryWrapper.like("title", searchVto.getLikeString());
        }
        if (searchVto.getType() != null) {
            queryWrapper.eq("type", searchVto.getType());
        }
        if (Objects.nonNull(searchVto.getUserId())) {
            queryWrapper.like("userId", searchVto.getUserId());
        }
        Page<Confess> confessPage = confessService.page(new Page<Confess>(searchVto.getPageIndex(), searchVto.getPageSize()), queryWrapper);
        confessPage.getRecords().forEach(confess -> {
            if (!confess.getImages().isEmpty()) {
                confess.setImgList(Arrays.asList(confess.getImages().split(",")));
            }
            //转Map
            confess.setMap(JSON.toJavaObject(JSON.parseObject(confess.getInfo()), Map.class));
            confess.setUser(userMapper.selectById(confess.getUserId()));
            confess.setLikeCount(likeMapper.selectCount(new QueryWrapper<Like>().eq("pkId", confess.getId())));
            confess.setCommentCount(commentMapper.selectCount(new QueryWrapper<Comment>().eq("pkId", confess.getId())));
            confess.setCollectCount(collectMapper.selectCount(new QueryWrapper<Collect>().eq("pkId", confess.getId())));
        });
        return confessPage;
    }

    @PostMapping("/commentList")
    @isLogin
    @BackUse
    @SystemLog(info = "后台获取所有评论内容")
    public Page<Comment> commentList(@RequestBody SearchVto searchVto) throws ResultException {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper();
        if (!searchVto.getLikeString().equals("")) {
            queryWrapper.like("info", searchVto.getLikeString()).or().like("reply", searchVto.getLikeString());
        }

        Page<Comment> commentPage = commentService.page(new Page<Comment>(searchVto.getPageIndex(), searchVto.getPageSize()), queryWrapper);
        if (!commentPage.getRecords().isEmpty()) {
            commentPage.getRecords().forEach(x -> {
                x.setConfess(confessService.getById(x.getPkId()));
                x.setUser(userMapper.selectById(x.getUserId()));
            });
        }
        return commentPage;
    }

    @PostMapping("/del")
    @Transactional(rollbackFor = Exception.class)
    @isLogin
    @BackUse
    @SystemLog(info = "后台删除信息")
    public void del(@RequestBody SearchVto searchVto) throws ResultException {
        //评论
        if (searchVto.getDelType().equals(0)) {
            commentService.removeById(searchVto.getId());
        } else if (searchVto.getDelType().equals(1)) {
            //发布内容
            Integer id = searchVto.getId();
            confessService.removeById(id);
            collectService.remove(new QueryWrapper<Collect>().eq("pkId", id));
            commentService.remove(new QueryWrapper<Comment>().eq("pkId", id));
            likeService.remove(new QueryWrapper<Like>().eq("pkId", id));
        } else if (searchVto.getDelType().equals(2)) {
            //后台用户
            if (searchVto.getId().equals(ThreadLocal.getThreadLocalAdmin().getId())) {
                throw new ResultException(ResultStatus.CAN_NOT_DEL_MINE);
            }
            adminService.removeById(searchVto.getId());
        } else {
            //敏感词
            sensitiveMapper.deleteById(searchVto.getId());
        }
    }

    @PostMapping("/adminList")
    @BackUse
    @isLogin
    @SystemLog(info = "获取所有后台用户")
    public Page<Admin> adminPage(@RequestBody SearchVto searchVto) throws ResultException {
        Page<Admin> adminPage = adminService.page(new Page<Admin>(searchVto.getPageIndex(), searchVto.getPageSize()), new QueryWrapper<Admin>());
        adminPage.setTotal(adminService.count());
        return adminPage;
    }

    @PostMapping("/addAdmin")
    @BackUse
    @isLogin
    @SystemLog(info = "添加修改后台用户")
    public void addAdmin(@RequestBody Admin admin) throws ResultException {
        if (admin.getId() == null) {
            verifyData(admin);
        } else {
            verifyUpdateData(admin);
        }
        adminService.saveOrUpdate(admin);
    }

    private void verifyUpdateData(Admin admin) throws ResultException {
        Admin userInfo = adminService.getById(admin.getId());
        if (!userInfo.getNumber().equals(admin.getNumber())) {
            if (adminService.count(new QueryWrapper<Admin>().eq("number", admin.getNumber())) > 0) {
                throw new ResultException(ResultStatus.NUMBER_ERROR);
            }
        }
        if (!userInfo.getPassword().equals(admin.getPassword())) {
            admin.setPassword(getMD5(admin.getPassword()));
        }
    }

    private void verifyData(Admin admin) throws ResultException {
        if (adminService.count(new QueryWrapper<Admin>().eq("number", admin.getNumber())) > 0) {
            throw new ResultException(ResultStatus.NUMBER_ERROR);
        } else {
            admin.setPassword(getMD5(admin.getPassword()));
        }
    }

    @PostMapping("/userList")
    @BackUse
    @isLogin
    @SystemLog(info = "获取所有App用户")
    public Page<User> userList(@RequestBody SearchVto searchVto) throws ResultException {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (!searchVto.getLikeString().equals("")) {
            queryWrapper.like("name", searchVto.getLikeString()).or().like("number", searchVto.getLikeString());
        }
        return userService.page(new Page<User>(searchVto.getPageIndex(), searchVto.getPageSize()), queryWrapper);
    }

    @Autowired
    SensitiveMapper sensitiveMapper;

    @PostMapping("/sensitive")
    @BackUse
    @isLogin
    @SystemLog(info = "获取所有敏感词信息")
    public Page<Sensitive> sensitivePage(@RequestBody SearchVto searchVto) throws ResultException {
        QueryWrapper<Sensitive> queryWrapper = new QueryWrapper();
        if (!searchVto.getLikeString().isEmpty()) {
            queryWrapper.like("info", searchVto.getLikeString()).or().like("number", searchVto.getLikeString());
        }
        return sensitiveMapper.selectPage(new Page<Sensitive>(searchVto.getPageIndex(), searchVto.getPageSize()), queryWrapper);
    }

    @Autowired
    LogMapper logMapper;

    @PostMapping("/logList")
    @BackUse
    @isLogin
    @SystemLog(info = "查看日志")
    public Page<Log> logList(@RequestBody SearchVto searchVto) throws ResultException {
        QueryWrapper<Log> queryWrapper = new QueryWrapper();
        if (!searchVto.getLikeString().isEmpty()) {
            queryWrapper.like("info", searchVto.getLikeString()).or().like("number", searchVto.getLikeString());
        }
        return logMapper.selectPage(new Page<Log>(searchVto.getPageIndex(), searchVto.getPageSize()), queryWrapper);
    }

    @PostMapping("/addsensitive")
    @BackUse
    @isLogin
    @SystemLog(info = "添加修改敏感词信息")
    public void addAdmin(@RequestBody Sensitive sensitive) throws ResultException {
        if (sensitive.getId() == null) {
            sensitiveMapper.insert(sensitive);
        } else {
            sensitiveMapper.updateById(sensitive);
        }
    }

    @PostMapping("/fenjin")
    @BackUse
    @isLogin
    @SystemLog(info = "封禁解封用户")
    public void fenjin(@RequestBody User user) throws ResultException {
         userMapper.updateById(user);
    }


}

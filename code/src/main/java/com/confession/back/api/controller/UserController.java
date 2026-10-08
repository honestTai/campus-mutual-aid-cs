package com.confession.back.api.controller;


import com.confession.back.api.bean.dto.MineDto;
import com.confession.back.api.bean.dto.UserLoginDto;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.User;
import com.confession.back.api.service.UserService;
import com.confession.back.config.filter.isLogin;
import com.confession.back.util.DataVerify;
import com.confession.back.util.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
public class UserController {

    /**
     * 登录，注册，我的发布-查看发布的内容评论，我的收藏，我的评论，删除评论，删除发布
     */

    @Autowired
    UserService userService;

    @Autowired
    DataVerify dataVerify;

    @PostMapping("/login")
    public UserLoginDto userLoginDto(@RequestBody User user) throws ResultException {
        return userService.login(user);
    }

    @PostMapping("/register")
    public void userRegister(@RequestBody User user) throws ResultException {
        dataVerify.verify(user);
        if (user.getImage().equals("")) {
            user.setImage("http://127.0.0.1:8776/api/confess/head.jpg");
        }
        userService.save(user);
    }

    @PostMapping("/update")
    @SystemLog(info = "修改个人信息")
    public void update(@RequestBody User user) throws ResultException {
        dataVerify.verifyById(user);
        userService.updateById(user);
    }

    /**
     * 个人中心模块
     */
    @GetMapping("/mine")
    @isLogin
    public MineDto mineDtoList() {
        return userService.mineDtoList();
    }

    @GetMapping("/mineInfo")
    @isLogin
    public User mineInfo() {
        return ThreadLocal.getThreadLocal();
    }
}


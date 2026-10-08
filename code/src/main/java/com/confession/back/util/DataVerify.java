package com.confession.back.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.entity.User;
import com.confession.back.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.confession.back.util.encryption.MD5Util.getMD5;

@Component
public class DataVerify {

    @Autowired
    UserService userService;

    //注册时用户信息验证
    public void verify(User user) throws ResultException {
        if (userService.count(new QueryWrapper<User>().eq("number", user.getNumber())) > 0) {
            throw new ResultException(ResultStatus.NUMBER_ERROR);
        } else {
            user.setPassword(getMD5(user.getPassword()));
        }
    }

    public void verifyById(User user) throws ResultException {
        User userInfo = userService.getById(user.getId());
        if (!userInfo.getNumber().equals(user.getNumber())) {
            if (userService.count(new QueryWrapper<User>().eq("number", user.getNumber())) > 0) {
                throw new ResultException(ResultStatus.NUMBER_ERROR);
            }
        }
        if (!userInfo.getPassword().equals(user.getPassword())) {
            user.setPassword(getMD5(user.getPassword()));
        }
    }
}

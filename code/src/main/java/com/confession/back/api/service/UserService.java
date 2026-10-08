package com.confession.back.api.service;

import com.confession.back.api.bean.dto.MineDto;
import com.confession.back.api.bean.dto.UserLoginDto;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * 
 */
public interface UserService extends IService<User> {

    UserLoginDto login(User user) throws ResultException;

    MineDto mineDtoList();
}

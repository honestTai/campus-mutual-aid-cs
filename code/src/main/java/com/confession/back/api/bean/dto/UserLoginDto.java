package com.confession.back.api.bean.dto;

import com.confession.back.api.entity.User;
import lombok.Data;

@Data
public class UserLoginDto {

    private Object user;

    private String token;

    public UserLoginDto(Object t, String token) {
        this.user = t;
        this.token = token;
    }
}

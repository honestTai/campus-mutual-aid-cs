package com.confession.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String info;

    private String methods;

    private String classname;
    private String user;
    private String params;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    public Log() {
    }

    public Log(String info, String methods, String classname, String user, String params) {
        this.info = info;
        this.methods = methods;
        this.classname = classname;
        this.user = user;
        this.params = params;
        this.time = new Date();
    }
}

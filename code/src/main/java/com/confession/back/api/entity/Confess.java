package com.confession.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Confess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表白
     */
    private String title;

    /**
     * 内容（json string)
     */
    private String info;

    /**
     * 0手机1qq2微信3其他
     */
    private Integer connect;

    /**
     * 其他
     */
    @TableField("elseConnect")
    private String elseConnect;

    /**
     * 图片
     */
    private String images;

    /**
     * （0表白1闲置2寻物3吐槽）
     */
    private Integer type;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 发布人
     */
    @TableField("userId")
    private Integer userId;

    @TableField(exist = false)
    private Map map;

    @TableField(exist = false)
    private List<String> imgList;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Integer likeCount;

    @TableField(exist = false)
    private Integer commentCount;

    @TableField(exist = false)
    private Integer collectCount;

    @TableField(exist = false)
    private Confess confess;

}

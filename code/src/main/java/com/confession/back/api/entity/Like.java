package com.confession.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "like_table")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 外键
     */
    @TableField("pkId")
    private Integer pkId;

    /**
     * 点赞本人
     */
    @TableField("userId")
    private Integer userId;

    /**
     * 点赞时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @TableField(exist = false)
    private Confess confess;


}

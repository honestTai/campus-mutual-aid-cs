package com.confession.back.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sun.dc.pr.PRError;

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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论信息
     */
    private String info;

    /**
     * 被评论内容
     */
    @TableField("pkId")
    private Integer pkId;

    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 父
     */
    @TableField("parentId")
    private Integer parentId;

    /**
     * 回复内容
     */
    private String reply;
    @TableField("replyTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyTime;

    @TableField(exist = false)
    private User user;
    @TableField("userId")
    private Integer userId;

    @TableField(exist = false)
    private Confess confess;


}

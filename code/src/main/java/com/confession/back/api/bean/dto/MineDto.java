package com.confession.back.api.bean.dto;

import com.confession.back.api.entity.Collect;
import com.confession.back.api.entity.Comment;
import com.confession.back.api.entity.Confess;
import com.confession.back.api.entity.Like;
import lombok.Data;

import java.util.List;

@Data
public class MineDto {

    //我的收藏
    private List<Collect> mineCollect;

    //我的发布
    private List<Confess> mineConfess;

    //我的评论
    private List<Comment> mineComment;

    private List<Like> likes;

    public MineDto(List<Collect> mineCollect, List<Confess> mineConfess, List<Comment> mineComment,List<Like> likes) {
        this.mineCollect = mineCollect;
        this.mineConfess = mineConfess;
        this.mineComment = mineComment;
        this.likes=likes;
    }
}

package com.confession.back.api.service.impl;

import com.confession.back.api.entity.Comment;
import com.confession.back.api.mapper.CommentMapper;
import com.confession.back.api.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * 
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

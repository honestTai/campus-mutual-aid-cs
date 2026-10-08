package com.confession.back.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.bean.vto.SearchVto;
import com.confession.back.api.entity.Collect;
import com.confession.back.api.entity.Comment;
import com.confession.back.api.entity.Confess;
import com.confession.back.api.entity.Like;
import com.confession.back.api.mapper.*;
import com.confession.back.api.service.ConfessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * 
 */
@Service
public class ConfessServiceImpl extends ServiceImpl<ConfessMapper, Confess> implements ConfessService {

    @Autowired
    ConfessMapper confessMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LikeMapper likeMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CollectMapper collectMapper;

    @Override
    public Page<Confess> selectConfessByPage(SearchVto searchVto) throws ParseException {
        QueryWrapper<Confess> confessQueryWrapper = new QueryWrapper<>();
        if (searchVto.getDateList() != null && searchVto.getDateList().size() > 1) {
            List<Date> dates = dateList(searchVto.getDateList());
            confessQueryWrapper.between("time", dates.get(0), dates.get(1));
        }
        if(!searchVto.getLikeString().equals("")){
            confessQueryWrapper.like("title",searchVto.getLikeString());
        }
        if (searchVto.getType().equals(0)) {
            confessQueryWrapper.eq("type", 0).or().eq("type", 3);
        } else {
            confessQueryWrapper.eq("type", searchVto.getType());
        }


        Page<Confess> confessList = confessMapper.selectPage(new Page<Confess>(searchVto.getPageIndex(), searchVto.getPageSize()), confessQueryWrapper);
        confessList.getRecords().forEach(confess -> {
            if (!confess.getImages().equals("")) {
                confess.setImgList(Arrays.asList(confess.getImages().split(",")));
            }
            //转Map
            confess.setMap(JSON.toJavaObject(JSON.parseObject(confess.getInfo()), Map.class));
            confess.setUser(userMapper.selectById(confess.getUserId()));
            confess.setLikeCount(likeMapper.selectCount(new QueryWrapper<Like>().eq("pkId", confess.getId())));
            confess.setCommentCount(commentMapper.selectCount(new QueryWrapper<Comment>().eq("pkId", confess.getId())));
            confess.setCollectCount(collectMapper.selectCount(new QueryWrapper<Collect>().eq("pkId", confess.getId())));
        });
        confessList.setTotal(confessMapper.selectCount(confessQueryWrapper));
        return confessList;
    }

    @Override
    public void creatConfess(Confess confess) {
        confess.setInfo(JSON.toJSONString(confess.getMap()));
        confess.setUserId(ThreadLocal.getThreadLocal().getId());
        confess.setTime(new Date());
        if (!confess.getImgList().isEmpty()) {
            confess.setImages(String.join(",", confess.getImgList()));
        } else {
            confess.setImages("");
        }
        confessMapper.insert(confess);
    }

    @Override
    public Confess getByIds(SearchVto searchVto) {
        Confess confess = confessMapper.selectById(searchVto.getId());

        if (!confess.getImages().equals("")) {
            confess.setImgList(Arrays.asList(confess.getImages().split(",")));
        }
        //转Map
        confess.setMap(JSON.toJavaObject(JSON.parseObject(confess.getInfo()), Map.class));
        confess.setUser(userMapper.selectById(confess.getUserId()));
        confess.setLikeCount(likeMapper.selectCount(new QueryWrapper<Like>().eq("pkId", confess.getId())));
        confess.setCommentCount(commentMapper.selectCount(new QueryWrapper<Comment>().eq("pkId", confess.getId())));
        confess.setCollectCount(collectMapper.selectCount(new QueryWrapper<Collect>().eq("pkId", confess.getId())));

        return confess;
    }


    private static List<Date> dateList(List<String> strings) throws ParseException {
        List<Date> dates = new ArrayList<>();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = fmt.parse(strings.get(0));
        Date date1 = fmt.parse(strings.get(1));
        dates.add(date);
        dates.add(date1);
        return dates;
    }
}

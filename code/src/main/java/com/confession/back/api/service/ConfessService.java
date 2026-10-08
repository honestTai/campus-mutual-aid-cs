package com.confession.back.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.confession.back.api.bean.vto.SearchVto;
import com.confession.back.api.entity.Confess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * 
 */
public interface ConfessService extends IService<Confess> {

    Page<Confess> selectConfessByPage(SearchVto searchVto) throws ParseException;

    void creatConfess(Confess confess);

    Confess getByIds(SearchVto searchVto);
}

package com.confession.back.api.service.impl;

import com.confession.back.api.entity.Admin;
import com.confession.back.api.mapper.AdminMapper;
import com.confession.back.api.service.AdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}

package com.confession.back.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.confession.back.api.entity.Admin;
import com.confession.back.api.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 *
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}

package com.confession.back.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.*;
import com.confession.back.api.mapper.LogMapper;
import com.confession.back.api.mapper.SensitiveMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.confession.back.util.jwt.JwtUtil.getNumber;

@Aspect
@Component
public class AopCheckInfo {

    @Autowired
    SensitiveMapper sensitiveMapper;


    /**
     * com.fdna.core.fdnaLog
     * 切点的获取
     * 获取所有带有此注解的方法
     */
    @Pointcut("@annotation(com.confession.back.util.CheckInfo)")
    public void methodAspect() {
    }


    /**
     * 前置通知
     * 检查是否有封禁与其他
     */
    @Before("methodAspect()")
    public void systemCheckBefore(JoinPoint joinPoint) throws ResultException, IllegalAccessException {
        User user = ThreadLocal.getThreadLocal();
        if (user.getStatus().equals(0)) {
            throw new ResultException(ResultStatus.CAN_NOT_USE);
        }

        List<String> stringList = sensitiveMapper.selectList(new QueryWrapper<Sensitive>().eq("status", 0))
                .stream()
                .map(Sensitive::getInfo)
                .collect(Collectors.toList());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Confess) {
                Confess confess = (Confess) arg;
                for (Field declaredField : confess.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);

                    // 处理 Map 类型参数
                    if (declaredField.getType().equals(Map.class)) {
                        Map<String, Object> params = (Map<String, Object>) declaredField.get(confess);
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                            for (String sensitiveWord : stringList) {
                                if(entry.getValue() instanceof String){
                                    if (entry.getValue().toString().contains(sensitiveWord)) {
                                        entry.setValue(entry.getValue().toString().replace(sensitiveWord, "*"));
                                    }
                                }

                            }
                        }
                        declaredField.set(confess, params);
                    }

                    // 处理字符串类型参数
                    if (declaredField.getType().equals(String.class)) {
                        String fieldValue = (String) declaredField.get(confess);
                        if (Objects.nonNull(fieldValue) && !fieldValue.isEmpty() && !declaredField.getName().equals("images")) {
                            for (String sensitiveWord : stringList) {
                                if (fieldValue.contains(sensitiveWord)) {
                                    fieldValue = fieldValue.replace(sensitiveWord, "*");
                                }
                            }
                            declaredField.set(confess, fieldValue);
                        }
                    }
                }
            }
            if (arg instanceof Comment) {
                Comment comment = (Comment) arg;
                for (Field declaredField : comment.getClass().getDeclaredFields()) {
                    declaredField.setAccessible(true);

                    // 处理字符串类型参数
                    if (declaredField.getType().equals(String.class)) {
                        String fieldValue = (String) declaredField.get(comment);
                        if (Objects.nonNull(fieldValue) && !fieldValue.isEmpty() && !declaredField.getName().equals("images")) {
                            for (String sensitiveWord : stringList) {
                                if (fieldValue.contains(sensitiveWord)) {
                                    fieldValue = fieldValue.replace(sensitiveWord, "*");
                                }
                            }
                            declaredField.set(comment, fieldValue);
                        }
                    }
                }
            }
        }
    }


}

package com.confession.back.util;

import com.alibaba.fastjson.JSONObject;
import com.confession.back.api.entity.Log;
import com.confession.back.api.mapper.LogMapper;
import com.confession.back.util.jwt.JwtUtil;
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
import java.lang.reflect.Method;
import java.util.Objects;

import static com.confession.back.util.jwt.JwtUtil.getNumber;
@Aspect
@Component
public class AopSystemLog {

    @Autowired
    LogMapper logMapper;


    private final static Logger SYSTEM_LOG = LoggerFactory.getLogger(AopSystemLog.class);

    /**
     * 开始时间
     */
    private long startTimeMillis = 0;

    /**
     * 参数
     */
    private String params;

    /**
     * 方法名
     */
    private String signatureName;

    /**
     * annotation
     */
    private boolean needResult;

    private static final String USER = "未获取到用户，未登录";

    /**
     * com.fdna.core.fdnaLog
     * 切点的获取
     * 获取所有带有此注解的方法
     */
    @Pointcut("@annotation(com.confession.back.util.SystemLog)")
    public void methodAspect() {
    }

    private Log log;

    /**
     * 前置通知
     * 提前构造好其他的参数
     */
    @Before("methodAspect()")
    public void systemLogBefore(JoinPoint joinPoint) {
        //开始时间
        this.startTimeMillis = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        this.signatureName = signature.getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //获取注解的值
        SystemLog annotation = method.getAnnotation(SystemLog.class);
        //打印最开始的请求参数
        SYSTEM_LOG.info("-------------------------- 操作开始：{} --------------------------", this.signatureName);
        SYSTEM_LOG.info("开始时间：{}", this.startTimeMillis);
        SYSTEM_LOG.info("接口内容：{}", annotation.info());
        SYSTEM_LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        SYSTEM_LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), this.signatureName);
        String token = request.getHeader("token");
        String name;
        if (Objects.isNull(token)) {
            SYSTEM_LOG.info("操作用户-登录账号: {}", USER);
            name = USER;
        } else {
            name = getNumber(token);
            if (Objects.nonNull(name)) {
                SYSTEM_LOG.info("操作用户-登录账号: {}", name);
            } else {
                name = USER;
                SYSTEM_LOG.info("操作用户-登录账号: {}", USER);
            }
        }
        //参数的解析
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        //判断是否打参数
        for (int i = 0; i < args.length; i++) {
            //哪些参数不打
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        this.params = JSONObject.toJSONString(arguments);
        SYSTEM_LOG.info("请求参数: {}", this.params);
        this.log = new Log(annotation.info(), request.getMethod(), this.signatureName, name, this.params);
    }

    /**
     * 异常通知
     * 日志状态为失败
     */
    @AfterThrowing(value = "methodAspect()", throwing = "exception")
    public void systemLogFail(JoinPoint joinPoint, Exception exception) {
        this.log.setStatus("失败");
        logMapper.insert(this.log);
        SYSTEM_LOG.info("-------------------------- 操作结束，异常：{} --------------------------", this.signatureName);
        SYSTEM_LOG.info("-------------------------- 异常原因：{} {} --------------------------", this.signatureName, exception);
        SYSTEM_LOG.info("-------------------------- {}操作结束 耗时：{} ms --------------------------", this.signatureName, System.currentTimeMillis() - this.startTimeMillis);
    }

    /**
     * 后置通知正常返回
     * 日志状态为正常
     */
    @AfterReturning(value = "methodAspect()", returning = "result")
    public void systemLogSuccess(JoinPoint joinPoint, Object result) {
        this.log.setStatus("成功");
        logMapper.insert(this.log);
        SYSTEM_LOG.info("-------------------------- 操作成功：{} --------------------------", this.signatureName);
        SYSTEM_LOG.info("-------------------------- {}操作结束 耗时：{} ms --------------------------", this.signatureName, System.currentTimeMillis() - this.startTimeMillis);
    }
}

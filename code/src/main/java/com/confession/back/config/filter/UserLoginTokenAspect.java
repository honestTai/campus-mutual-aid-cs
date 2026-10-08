package com.confession.back.config.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.confession.back.api.bean.general.result.ResultException;
import com.confession.back.api.bean.general.result.ResultStatus;
import com.confession.back.api.bean.threadLocal.ThreadLocal;
import com.confession.back.api.entity.Admin;
import com.confession.back.api.entity.User;
import com.confession.back.api.service.AdminService;
import com.confession.back.api.service.UserService;
import com.confession.back.util.BackUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

import static com.confession.back.util.jwt.JwtUtil.getNumber;
import static com.confession.back.util.jwt.JwtUtil.verify;


/**
 * 登录拦截实现类，实现登录拦截
 */

public class UserLoginTokenAspect implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws ResultException {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(isLogin.class)) {
            if (method.getAnnotation(isLogin.class).required()) {
                // 从 http 请求头中取出 token
                String token = httpServletRequest.getHeader("token");
                //判断有没有token
                if (token == null) {
                    throw new ResultException(ResultStatus.NO_LOGIN);
                }
                // 验证 token
                verifyToken(token,method);
                return true;
            }
        }
        return true;
    }

    //验证Token是否正确，是否有效
    private void verifyToken(String token,Method method) throws ResultException {
        //解析得到用户账号
        String number = getNumber(token);
        User user = userService.getOne(new QueryWrapper<User>().eq("number", number));
        if (user != null) {
            if (!verify(token, user.getNumber(), user.getPassword())) {
                throw new ResultException(ResultStatus.NO_LOGIN);
            }
            if(method.isAnnotationPresent(BackUse.class)){
                throw new ResultException(ResultStatus.NO_LOGIN);
            }
            ThreadLocal.setThreadLocal(user);
        } else {
            Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("number", number));
            if (admin != null) {
                if (!verify(token, admin.getNumber(), admin.getPassword())) {
                    throw new ResultException(ResultStatus.NO_LOGIN);
                }
                ThreadLocal.setThreadLocal(admin);
            } else {
                throw new ResultException(ResultStatus.NO_LOGIN);
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

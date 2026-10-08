package com.confession.back.api.bean.threadLocal;

import com.confession.back.api.entity.Admin;
import com.confession.back.api.entity.User;

/**
 * 后台用户本地守护线程类
 */
public class ThreadLocal {

    private static java.lang.ThreadLocal<Object> threadLocal = new java.lang.ThreadLocal();

    /**
     * 设置用户信息
     */
    public static void setThreadLocal(Object t) {
        threadLocal.set(t);
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static User getThreadLocal() {
        User user= (User) threadLocal.get();
        return user;
    }

    public static Admin getThreadLocalAdmin() {
        Admin admin= (Admin) threadLocal.get();
        return admin;
    }


}

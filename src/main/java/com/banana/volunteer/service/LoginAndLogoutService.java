package com.banana.volunteer.service;

import com.banana.volunteer.entity.User;

public interface LoginAndLogoutService {

    /**
     * 1.登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户实体
     */
    User login(String username, String password);

    /**
     * 2.注销
     *
     * @return 0/1 是否成功
     */
    boolean logout();
}

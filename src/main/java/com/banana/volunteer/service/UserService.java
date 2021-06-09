package com.banana.volunteer.service;


import com.banana.volunteer.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 1.通过用户名查找用户信息
     *
     * @param userName 用户名
     * @return 用户实体
     */
    User findByUserName(String userName);

    /**
     * 2.查找所有用户
     *
     * @return 用户实体列表
     */
    List<User> findAll();

    /**
     * 3.通过用户id查找
     *
     * @param userId 用户id
     * @return 用户实体
     */
    User findByUserId(Integer userId);

    /**
     * 4.更新用户
     *
     * @param user 用户实体
     * @return 用户实体
     */
    User updateUser(User user);

    /**
     * 5.根据用户id删除
     *
     * @param userId 用户id
     */
    void deleteByUserId(Integer userId);
}

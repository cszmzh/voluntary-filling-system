package com.banana.volunteer.service;

public interface CheckLoginService {

    /**
     * 1.检验管理员身份
     *
     * @return 布尔值 是否通过
     */
    boolean verification_ADMIN();

    /**
     * 2.检验超级管理员身份
     *
     * @return 布尔值 是否通过
     */
    boolean verification_ROOT();
}

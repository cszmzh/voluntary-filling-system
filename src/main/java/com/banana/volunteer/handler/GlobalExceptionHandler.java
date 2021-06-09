package com.banana.volunteer.handler;

import com.banana.volunteer.exception.NoLoginExeption;
import com.banana.volunteer.service.LoginAndLogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LoginAndLogoutService loginAndLogoutService;

    /**
     * 处理全局的未登录异常
     */
    @ExceptionHandler(NoLoginExeption.class)
    public void noLoginExceptionHandler(NoLoginExeption e) {
        loginAndLogoutService.logout();
    }
}

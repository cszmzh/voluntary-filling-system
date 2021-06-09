package com.banana.volunteer.aspect;

import com.banana.volunteer.enums.ResultEnum;
import com.banana.volunteer.exception.NoLoginExeption;
import com.banana.volunteer.holder.UserOrgHolder;
import com.banana.volunteer.holder.UserStatusHolder;
import com.banana.volunteer.service.CheckLoginService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class VerificationAspect {

    @Autowired
    private CheckLoginService checkLoginService;

    @Pointcut("execution(* com.banana.volunteer.controller.*.*ADMIN(..))")
    public void checkAdmin() {

    }

    @Pointcut("execution(* com.banana.volunteer.controller.*.*ROOT(..))")
    public void checkRoot() {

    }

    /**
     * 1.检验管理员及以上级别身份
     */
    @Around("checkAdmin()")
    public Object verification_ADMIN(ProceedingJoinPoint joinPoint) throws Throwable {
        //如果没有登录 这里要抛出异常
        if (!checkLoginService.verification_ADMIN()) {
            throw new NoLoginExeption(ResultEnum.LOGIN_ERROR);
        }
        Object obj = joinPoint.proceed();
        UserStatusHolder.remove();
        UserOrgHolder.remove();
        return obj;
    }

    /**
     * 2.检验超级管理员身份
     */
    @Around("checkRoot()")
    public Object verification_ROOT(ProceedingJoinPoint joinPoint) throws Throwable {
        // 如果没有登录 这里要抛出异常
        if (!checkLoginService.verification_ROOT()) {
            throw new NoLoginExeption(ResultEnum.LOGIN_ERROR);
        }
        Object obj = joinPoint.proceed();
        UserStatusHolder.remove();
        UserOrgHolder.remove();
        return obj;
    }
}

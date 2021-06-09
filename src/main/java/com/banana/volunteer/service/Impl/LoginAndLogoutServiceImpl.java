package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.User;
import com.banana.volunteer.enums.ResultEnum;
import com.banana.volunteer.exception.BusinessException;
import com.banana.volunteer.utils.DesUtil;
import com.banana.volunteer.utils.RequestAndResponseUtil;
import com.banana.volunteer.service.LoginAndLogoutService;
import com.banana.volunteer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
@Slf4j
public class LoginAndLogoutServiceImpl implements LoginAndLogoutService {

    @Autowired
    private UserService userService;

    @Override
    public User login(String username, String password) {

        User user = userService.findByUserName(username);
        if (user == null) {
            log.error("【登录失败】用户不存在，用户名：{}", username);
            throw new BusinessException(ResultEnum.USER_NULL);
        }

        //验证密码
        if (user.getUserPassword().equals(password)) {
            //设置cookie，注意要des加密
            Cookie cookie = new Cookie("uid", DesUtil.encrypt(username));
            cookie.setMaxAge(60 * 60 * 24 * 7);   //设置7天有效期
            cookie.setPath("/");
            RequestAndResponseUtil.getResponse().addCookie(cookie);
            log.info("【登陆成功】用户名：{}", username);
            return user;
        } else {
            log.error("【登录失败】用户名：{} , 密码：{} ", username, password);
        }
        return null;
    }

    @Override
    public boolean logout() {
        Cookie[] cookies = RequestAndResponseUtil.getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uid")) {
                cookie.setPath("/");    //注意要设置作用路径
                cookie.setMaxAge(0);
                RequestAndResponseUtil.getResponse().addCookie(cookie);
                return true;
            }
        }
        return false;
    }
}

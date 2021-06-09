package com.banana.volunteer.controller;

import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.VO.userInfoVO.LoginResult;
import com.banana.volunteer.entity.User;
import com.banana.volunteer.service.LoginAndLogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginAndLogoutController {

    @Autowired
    private LoginAndLogoutService loginAndLogoutService;

    /**
     * 1.登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return ResultVO
     */
    @PostMapping("/login")
    public ResultVO login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = loginAndLogoutService.login(username, password);
        if (user != null) {

            LoginResult loginResult = new LoginResult();
            loginResult.setUsername(username);
            loginResult.setStatus(user.getUserStatus());

            ResultVO resultVO = new ResultVO(0, "登录成功");
            resultVO.setData(loginResult);

            return resultVO;
        }
        return new ResultVO(1, "登录失败");
    }

    /**
     * 2.注销接口
     */
    @PostMapping("/logout")
    public ResultVO logout() {
        if (loginAndLogoutService.logout()) {
            return new ResultVO(0, "注销成功");
        }
        return new ResultVO(1, "注销失败");
    }
}

package com.banana.volunteer.service.Impl;

import com.banana.volunteer.entity.User;
import com.banana.volunteer.enums.ResultEnum;
import com.banana.volunteer.enums.UserStatusEnum;
import com.banana.volunteer.exception.NoLoginException;
import com.banana.volunteer.holder.UserIdHolder;
import com.banana.volunteer.utils.DesUtil;
import com.banana.volunteer.utils.RequestAndResponseUtil;
import com.banana.volunteer.holder.UserOrgHolder;
import com.banana.volunteer.holder.UserStatusHolder;
import com.banana.volunteer.service.CheckLoginService;
import com.banana.volunteer.service.OrganizationService;
import com.banana.volunteer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
@Slf4j
public class CheckLoginServiceImpl implements CheckLoginService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public boolean verification_ADMIN() {

        Cookie[] cookies = RequestAndResponseUtil.getRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uid")) {
                User user;
                try {
                    user = userService.findByUserName(DesUtil.decrypt(cookie.getValue()));
                } catch (Exception e) {
                    // 解析失败
                    throw new NoLoginException(ResultEnum.LOGIN_ERROR);
                }

                if (user == null) {
                    log.error("【验证用户失败】找不到用户：{}", DesUtil.decrypt(cookie.getValue()));
                    return false;
                } else if (user.getUserStatus().equals(UserStatusEnum.ADMIN.getCode())) {
                    log.info("【验证管理员身份成功】用户名:{}", DesUtil.decrypt(cookie.getValue()));
                    UserIdHolder.set(user.getUserId().toString());
                    UserStatusHolder.set(user.getUserStatus().toString());                                       // 设置权限状态标识
                    UserOrgHolder.set(organizationService.findOrgIdByBranchId(user.getBranchId()).toString());   // 设置学生所属组织id
                    return true;
                } else if (user.getUserStatus().equals(UserStatusEnum.ROOT.getCode())) {
                    log.info("【验证ROOT身份成功】用户名:{}", DesUtil.decrypt(cookie.getValue()));
                    UserIdHolder.set(user.getUserId().toString());
                    UserStatusHolder.set(user.getUserStatus().toString());
                    UserOrgHolder.set(organizationService.findOrgIdByBranchId(user.getBranchId()).toString());   // 设置学生所属组织id
                    return true;
                }
            }
        }
        log.error("【验证用户失败】cookie不存在");
        return false;
    }

    public boolean verification_ROOT() {

        Cookie[] cookies = RequestAndResponseUtil.getRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uid")) {
                User user = null;

                try {
                    user = userService.findByUserName(DesUtil.decrypt(cookie.getValue()));
                } catch (Exception e) {
                    // 解析失败
                    throw new NoLoginException(ResultEnum.LOGIN_ERROR);
                }

                if (user == null) {
                    log.error("【验证用户失败】找不到用户：{}", DesUtil.decrypt(cookie.getValue()));
                    return false;
                } else if (user.getUserStatus().equals(UserStatusEnum.ROOT.getCode())) {
                    log.info("【验证ROOT身份成功】用户名:{}", DesUtil.decrypt(cookie.getValue()));
                    UserIdHolder.set(user.getUserId().toString());
                    UserStatusHolder.set(user.getUserStatus().toString());
                    UserOrgHolder.set(organizationService.findOrgIdByBranchId(user.getBranchId()).toString());
                    return true;
                }
            }
        }
        log.error("【验证用户失败】cookie不存在");
        return false;
    }
}

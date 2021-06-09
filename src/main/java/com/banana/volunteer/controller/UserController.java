package com.banana.volunteer.controller;

import com.banana.volunteer.VO.ResultVO;
import com.banana.volunteer.VO.userInfoVO.UserResult;
import com.banana.volunteer.entity.User;
import com.banana.volunteer.enums.UserStatusEnum;
import com.banana.volunteer.holder.UserIdHolder;
import com.banana.volunteer.service.OrganizationService;
import com.banana.volunteer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService orgService;

    /**
     * 1.获取所有管理员账号信息
     */
    @GetMapping("/getAll")
    public ResultVO getAll_ROOT() {

        log.info("[user/getAll]获取所有管理员，操作者id[{}]", UserIdHolder.get());

        List<User> userList = userService.findAll();
        List<UserResult> userResultList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUserStatus() == 2) {
                continue;
            }
            UserResult userResult = new UserResult();
            userResult.setUserId(user.getUserId());
            userResult.setUserName(user.getUserName());
            userResult.setRealName(user.getRealName());
            userResult.setUserPassword(user.getUserPassword());
            userResult.setOrganization(orgService.findOrgNameByBranchId(user.getBranchId()));
            userResult.setBranch(orgService.findBranchNameByBranchId(user.getBranchId()));
            userResult.setUserStatus(user.getUserStatus());
            userResult.setCreateTime(user.getCreateTime());
            userResultList.add(userResult);
        }

        return new ResultVO(0, "success", userResultList);
    }

    /**
     * 2.更新账号信息
     */
    @PostMapping("/update")
    public ResultVO updateUser_ROOT(@RequestParam("userId") Integer userId, @RequestParam("userName") String userName,
                                    @RequestParam("realName") String realName, @RequestParam("password") String password,
                                    @RequestParam("branchId") Integer branchId) {

        log.info("[user/update]更新管理员 userId=[{}]，操作者id[{}]", userId, UserIdHolder.get());

        User user = userService.findByUserId(userId);
        user.setUserName(userName);
        user.setRealName(realName);
        user.setUserPassword(password);
        user.setBranchId(branchId);
        userService.updateUser(user);
        return new ResultVO(0, "success");
    }

    /**
     * 3.创建管理员账号
     */
    @PostMapping("/createAdmin")
    public ResultVO createAdmin_ROOT(@RequestParam("userName") String username, @RequestParam("realName") String realName,
                                     @RequestParam("password") String password, @RequestParam("branchId") Integer branchId) {

        log.info("[user/create]创建管理员 username=[{}]，操作者id[{}]", username, UserIdHolder.get());

        User user = new User();
        user.setUserName(username);
        user.setRealName(realName);
        user.setUserPassword(password);
        user.setBranchId(branchId);
        user.setUserStatus(UserStatusEnum.ADMIN.getCode());
        userService.updateUser(user);
        return new ResultVO(0, "success");
    }

    /**
     * 4.删除管理员账号
     */
    @PostMapping("/delete")
    public ResultVO deleteUser_ROOT(@RequestParam("userId") Integer userId) {

        log.info("[user/delete]删除管理员 userId=[{}]，操作者id[{}]", userId, UserIdHolder.get());

        User user = userService.findByUserId(userId);
        if (user.getUserStatus() == 2) {
            return new ResultVO(1, "无法删除超级管理员");
        }
        userService.deleteByUserId(userId);
        return new ResultVO(0, "success");
    }
}

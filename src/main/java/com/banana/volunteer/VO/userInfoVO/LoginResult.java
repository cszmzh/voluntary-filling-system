package com.banana.volunteer.VO.userInfoVO;

import lombok.Data;

@Data
public class LoginResult {

    //用户名
    private String username;

    //用户权限
    private Integer status;

}

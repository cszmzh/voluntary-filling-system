package com.banana.volunteer.VO.userInfoVO;

import lombok.Data;

import java.util.Date;

@Data
public class UserResult {
    private Integer userId;
    private String userName;
    private String realName;
    private String userPassword;
    private String organization;
    private String branch;
    private Integer userStatus;
    private Date createTime;
}

package com.banana.volunteer.enums;

import lombok.Getter;

@Getter
public enum UserStatusEnum {

    ADMIN(1, "管理员"),
    ROOT(2, "超级管理员");

    private Integer code;
    private String message;

    UserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

package com.banana.volunteer.enums;

import lombok.Getter;

@Getter
public enum EnrollStatusEnum {

    UNDER_ENROLL(0, "未被录取"),
    FIRST_ENROLL(1, "一志愿录取"),
    SECOND_ENROLL(2, "二志愿录取");

    private Integer code;

    private String message;

    EnrollStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

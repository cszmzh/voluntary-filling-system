package com.banana.volunteer.exception;

import com.banana.volunteer.enums.ResultEnum;

public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

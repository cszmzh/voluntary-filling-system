package com.banana.volunteer.exception;

import com.banana.volunteer.enums.ResultEnum;

public class NoLoginException extends RuntimeException {

    private Integer code;

    public NoLoginException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public NoLoginException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

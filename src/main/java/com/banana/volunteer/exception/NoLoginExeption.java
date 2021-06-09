package com.banana.volunteer.exception;

import com.banana.volunteer.enums.ResultEnum;

public class NoLoginExeption extends RuntimeException {

    private Integer code;

    public NoLoginExeption(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public NoLoginExeption(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

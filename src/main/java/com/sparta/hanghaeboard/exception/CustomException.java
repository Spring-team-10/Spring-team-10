package com.sparta.hanghaeboard.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private int statuscode;
    private String msg;

    public CustomException(ErrorCode errorCode) {
        this.statuscode = errorCode.getHttpStatus().value();
        this.msg = errorCode.getMessage();
    }
}

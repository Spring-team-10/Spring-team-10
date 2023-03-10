package com.sparta.hanghaeboard.dto;

import com.sparta.hanghaeboard.exception.CustomException;
import com.sparta.hanghaeboard.exception.SuccessCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@Setter
public class MsgResponseDto { //전체 응답 Dto

    private int statusCode; //상태코드
    private String msg;     //확인메세지

    public MsgResponseDto(){
        this.statusCode = 200;
        this.msg = "성공";

    }

    public MsgResponseDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public MsgResponseDto(SuccessCode successCode) {
        this.msg = successCode.getMessage();
        this.statusCode = successCode.getHttpStatus().value();
    }

    public MsgResponseDto(CustomException customException) {
        this.msg = customException.getMsg();
        this.statusCode = customException.getStatuscode();
    }

    public MsgResponseDto(MethodArgumentNotValidException ex) {
        this.msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        this.statusCode = HttpStatus.BAD_REQUEST.value();
    }
}
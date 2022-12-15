package com.sparta.hanghaeboard.dto;

import lombok.Getter;
import lombok.Setter;

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
}
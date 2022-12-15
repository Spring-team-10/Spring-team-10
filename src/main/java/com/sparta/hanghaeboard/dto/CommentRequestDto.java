package com.sparta.hanghaeboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@AllArgsConstructor RequestDto 필요 없는 이유 찾아보기!
public class CommentRequestDto {

    private String comment;

}

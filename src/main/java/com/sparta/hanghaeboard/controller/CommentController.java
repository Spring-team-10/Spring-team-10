package com.sparta.hanghaeboard.controller;

import com.sparta.hanghaeboard.dto.CommentRequestDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.security.UserDetailsImpl;
import com.sparta.hanghaeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;    //connect to commentService

    //DB insert 댓글 작성
    @PostMapping("/{id}")
    public ResponseEntity<MsgResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 응답 보내기
        return ResponseEntity.ok(commentService.createComment(id, commentRequestDto, userDetails.getUser()));
    }

    //DB update 댓글 수정
    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<MsgResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentRequestDto, userDetails.getUser()));
    }

    //DB delete 댓글 삭제
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<MsgResponseDto> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.deleteComment(postId, commentId, userDetails.getUser()));
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<MsgResponseDto> commentLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.commentLike(id, userDetails.getUser()));
    }
}

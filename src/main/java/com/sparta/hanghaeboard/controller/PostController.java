package com.sparta.hanghaeboard.controller;

import com.sparta.hanghaeboard.dto.PostRequestDto;
import com.sparta.hanghaeboard.dto.PostResponseDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.security.UserDetailsImpl;
import com.sparta.hanghaeboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<MsgResponseDto> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.createPost(requestDto, userDetails.getUser()));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getListPost() {
        return ResponseEntity.ok(postService.getListPost());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<MsgResponseDto> getPost(
            @PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<MsgResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.update(id, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<MsgResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.deletePost(id, userDetails.getUser()));
    }

    @PostMapping("/posts/like/{id}")
    public ResponseEntity<MsgResponseDto> postLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.postLike(id, userDetails.getUser()));
    }
}

package com.sparta.hanghaeboard.dto;

import com.sparta.hanghaeboard.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto extends MsgResponseDto {
    private Long id;
    private String username;
    private String content;
    private String title;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;
    private int postLikesCount;
    private List<CommentResponseDto> commentList = new ArrayList<>();


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLikesCount = post.getPostLikes().size();
    }

    public PostResponseDto(Post post, List<CommentResponseDto> commentList) { //오버로딩
        this.id = post.getId();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = commentList;
        this.postLikesCount = post.getPostLikes().size();
    }
}
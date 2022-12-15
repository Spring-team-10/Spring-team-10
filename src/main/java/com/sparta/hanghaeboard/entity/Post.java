package com.sparta.hanghaeboard.entity;

import com.sparta.hanghaeboard.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 225, nullable = false)
    private String username;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post" ,cascade = CascadeType.REMOVE)// 글 하나가 삭제되면 맵핑되어있는 쪽 테이블이름!!! 글도 삭제되는 cascade 연속성 전이 속성
    @OrderBy("createdAt desc")// 엔티티단에서 정렬
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.username = user.getUsername();
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
    }
}
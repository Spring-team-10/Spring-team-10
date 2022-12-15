package com.sparta.hanghaeboard.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    public PostLike(Post post, User user){
        this.post = post;
        this.user = user;
    }
}

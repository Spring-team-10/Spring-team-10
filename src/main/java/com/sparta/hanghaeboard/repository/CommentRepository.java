package com.sparta.hanghaeboard.repository;

import com.sparta.hanghaeboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}

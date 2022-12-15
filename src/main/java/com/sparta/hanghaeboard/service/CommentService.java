package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.CommentRequestDto;
import com.sparta.hanghaeboard.dto.CommentResponseDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.entity.*;
import com.sparta.hanghaeboard.repository.CommentLikeRepository;
import com.sparta.hanghaeboard.repository.CommentRepository;
import com.sparta.hanghaeboard.repository.PostRepository;
import com.sparta.hanghaeboard.util.ErrorCode;
import com.sparta.hanghaeboard.util.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, User user) {

        // 게시글의 DB 저장 유무 확인
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
        );
        // 요청 받은 DTO로 DB에 저장할 객체 만들기
        Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

        commentRepository.save(comment);

        return new CommentResponseDto(comment);

    }

    @Transactional
    public CommentResponseDto updateComment(Long id, Long commentId, CommentRequestDto commentRequestDto, User user) {
        // 게시글의 DB 저장 유무 확인
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
        );

        Comment comment;
        //유저의 권한이 admin과 같거나 작성자와 같으면 데이터 수정 가능
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            // 입력 받은 댓글 id와 일치하는 DB 조회
            comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new RequestException(ErrorCode.댓글이_존재하지_않습니다_400));
        } else {
            // 입력 받은 댓글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode.작성자만_수정할_수_있습니다_400)
            );
        }
        // 요청 받은 DTO로 DB에 업데이트
        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);

    }

    @Transactional
    public MsgResponseDto deleteComment(Long id, Long commentId, User user) {
        // 게시글의 DB 저장 유무 확인
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
        );

        Comment comment;
        //유저의 권한이 admin과 같으면 모든 데이터 삭제 가능
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new RequestException(ErrorCode.댓글이_존재하지_않습니다_400));
        } else {
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode.작성자만_삭제할_수_있습니다_400)
            );
        }

        commentRepository.delete(comment);

        return new MsgResponseDto(HttpStatus.OK.value(),"댓글 삭제 성공");
    }

    @Transactional
    public MsgResponseDto commentLike(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.댓글이_존재하지_않습니다_400)
        );
        if(commentLikeRepository.findByUserIdAndCommentId(user.getId(),comment.getId()).isEmpty()){
            commentLikeRepository.save(new CommentLike(comment, user));
            return new MsgResponseDto(HttpStatus.OK.value(),"좋아요 성공");
        }else{
            commentLikeRepository.deleteByUserIdAndCommentId(user.getId(),comment.getId());
            return new MsgResponseDto(HttpStatus.OK.value(),"좋아요 취소");
        }
    }

}



package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.CommentResponseDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.dto.PostRequestDto;
import com.sparta.hanghaeboard.dto.PostResponseDto;
import com.sparta.hanghaeboard.entity.*;
import com.sparta.hanghaeboard.repository.PostLikeRepository;
import com.sparta.hanghaeboard.repository.PostRepository;
import com.sparta.hanghaeboard.util.ErrorCode;
import com.sparta.hanghaeboard.util.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;                                      //post repo connect
    private final PostLikeRepository postLikeRepository;

    //Post Create function 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        //1. create post Object and insert DB
        Post post = postRepository.save(new Post(requestDto, user));                 //Dto->Entity, DB Save
        return new PostResponseDto(post);                                            // Return Response Entity
    }

    //Get posts from DB (all) 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getListPost() {
        //1. Select All Post
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();       //select All

        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for (Post post : postList) {

            List<CommentResponseDto> commentList = new ArrayList<>();        //Put commentList into each Post
            for (Comment comment : post.getCommentList()) {     //Comment Entity -> CommentResponseDto (in commentList)
                commentList.add(new CommentResponseDto(comment));
            }

            PostResponseDto postDto = new PostResponseDto(post, commentList);
            postResponseDto.add(postDto);
        }
        return postResponseDto;
    }

    //Get post from DB (one) 게시글 상세조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(                         //select one
                () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
        );
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }
        return new PostResponseDto(post, commentList);                               //Entity -> Dto
    }

    //DB update function 게시글 수정
    @Transactional //트랜잭션 어노테이션 필요 -> DB 변화 감지
    public PostResponseDto update(Long id, PostRequestDto requestDto, User user) {
        Post post;
        //1. check auth 권한 조회
        //유저의 권한이 admin과 같은 경우
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            post = postRepository.findById(id).orElseThrow(                          //find post
                    () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400));
        } else {
            //유저의 권한이 admin이 아니면 아이디가 같은 유저인 경우                             //find post
            post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode.작성자만_수정할_수_있습니다_400)
            );
        }
        //2. 게시글 수정 + 업데이트
        post.update(requestDto);                                                     //DB update

        return new PostResponseDto(post);
    }

    //DB delete function 게시글 삭제
    @Transactional
    public MsgResponseDto deletePost(Long id, User user) {
        Post post;
        //1.권한 조회
        //유저의 권한이 admin과 같으면 모든 데이터 삭제 가능
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {                                      //find post
            post = postRepository.findById(id).orElseThrow(
                    () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400));
        } else {
            //유저의 권한이 admin이 아니면 아이디가 같은 유저만 삭제 가능
            post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new RequestException(ErrorCode.작성자만_삭제할_수_있습니다_400)
            );
        }
        //2.게시글 삭제
        postRepository.delete(post);                                                       //DB delete

        return new MsgResponseDto(HttpStatus.OK.value(), "게시글 삭제 성공");
    }

    @Transactional
    public MsgResponseDto postLike(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.게시글이_존재하지_않습니다_400)
        );
        if(postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId()).isEmpty()){
            postLikeRepository.save(new PostLike(post, user));
            return new MsgResponseDto(HttpStatus.OK.value(),"좋아요 성공");
        }else{
            postLikeRepository.deleteByUserIdAndPostId(user.getId(),post.getId());
            return new MsgResponseDto(HttpStatus.OK.value(),"좋아요 취소");
        }
    }
}

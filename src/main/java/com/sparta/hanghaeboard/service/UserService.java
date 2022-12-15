package com.sparta.hanghaeboard.service;

import com.sparta.hanghaeboard.dto.LoginRequestDto;
import com.sparta.hanghaeboard.dto.SignupRequestDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.entity.User;
import com.sparta.hanghaeboard.entity.UserRoleEnum;
import com.sparta.hanghaeboard.jwt.JwtUtil;
import com.sparta.hanghaeboard.repository.UserRepository;
import com.sparta.hanghaeboard.util.ErrorCode;
import com.sparta.hanghaeboard.util.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RequestException(ErrorCode.중복된_아이디_입니다_400);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new RequestException(ErrorCode.관리자_비밀번호가_일치하지_않습니다_400);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RequestException(ErrorCode.회원을_찾을_수_없습니다_400)
        );
        // 비밀번호 확인
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw  new RequestException(ErrorCode.비밀번호가_일치하지_않습니다_400);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }

    @Transactional
    public ResponseEntity<MsgResponseDto> deleteUser(Long id, User user) {

        //유저의 권한이 admin과 같으면 모든 데이터 삭제 가능
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            user = userRepository.findById(id).orElseThrow(
                    () -> new RequestException(ErrorCode. 관리자_비밀번호가_일치하지_않습니다_400));
        } else {
            //유저의 권한이 admin이 아니면 아이디가 같은 유저만 삭제 가능
            user = userRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                    () -> new RequestException(ErrorCode.회원을_찾을_수_없습니다_400)
            );
        }
        userRepository.delete(user);

        return ResponseEntity.ok(new MsgResponseDto(HttpStatus.OK.value(), "회원 탈퇴 성공"));
    }
}

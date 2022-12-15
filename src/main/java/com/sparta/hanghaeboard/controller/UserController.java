package com.sparta.hanghaeboard.controller;

import com.sparta.hanghaeboard.dto.LoginRequestDto;
import com.sparta.hanghaeboard.dto.SignupRequestDto;
import com.sparta.hanghaeboard.dto.MsgResponseDto;
import com.sparta.hanghaeboard.security.UserDetailsImpl;
import com.sparta.hanghaeboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MsgResponseDto> signup(
            @RequestBody @Valid SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok(new MsgResponseDto( HttpStatus.OK.value(),"회원가입 성공"));
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgResponseDto(HttpStatus.OK.value(),"로그인 성공"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MsgResponseDto> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(id, userDetails.getUser());
    }
}
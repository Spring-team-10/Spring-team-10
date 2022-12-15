package com.sparta.hanghaeboard.security;


import com.sparta.hanghaeboard.entity.User;
import com.sparta.hanghaeboard.entity.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user; // 인증완료된 User 객체
    private final String username; // 인증완료된 User의 ID

    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }
    //  인증완료된 User 를 가져오는 Getter
    public User getUser() {
        return user;
    }
    //사용자의 권한 GrantedAuthority 로 추상화 및 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }
    //사용자의 ID, PWD Getter
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
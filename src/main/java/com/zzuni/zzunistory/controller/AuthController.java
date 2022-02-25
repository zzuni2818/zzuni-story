package com.zzuni.zzunistory.controller;

import com.zzuni.zzunistory.config.JwtProvider;
import com.zzuni.zzunistory.dto.UserDto;
import com.zzuni.zzunistory.service.UserService;
import com.zzuni.zzunistory.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<UserDto.ResLoginDto> login(@RequestBody UserDto.ReqLoginDto reqLoginDto) throws UsernameNotFoundException {
        UserVo userVo = userService.loadUserByUsername(reqLoginDto.getUsername());
        if(!passwordEncoder.matches(reqLoginDto.getPassword(), userVo.getPassword())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        String accessToken = jwtProvider.createAccessToken(userVo);
        String refreshToken = jwtProvider.createRefreshToken();
        //TODO
        //regist refreshToken on db
        return ResponseEntity.ok(new UserDto.ResLoginDto(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody UserDto.ReqLogoutDto reqLogoutDto) {
        System.out.println("onLogout, username: " + reqLogoutDto.getUsername());
        //unregist refreshToken on db

        return new ResponseEntity(HttpStatus.OK);
    }

    //TODO
    // /refresh_token
}

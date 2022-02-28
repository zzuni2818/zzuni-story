package com.zzuni.zzunistory.controller;

import com.zzuni.zzunistory.config.JwtProvider;
import com.zzuni.zzunistory.dto.AuthDto;
import com.zzuni.zzunistory.service.AuthService;
import com.zzuni.zzunistory.vo.JwtTokenVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.ResLoginDto> login(@RequestBody AuthDto.ReqLoginDto reqLoginDto, HttpServletResponse response) {
        JwtTokenVo jwtTokenVo = null;
        try {
            jwtTokenVo = authService.login(reqLoginDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Date now = new Date();
        Date expire = jwtProvider.getExpires(jwtTokenVo.getRefreshToken());

        Cookie setCookie = new Cookie("refreshToken", jwtTokenVo.getRefreshToken());
        setCookie.setPath("/");
        setCookie.setMaxAge((int) ((expire.getTime() - now.getTime()) / 1000));
        setCookie.setHttpOnly(true);
        // TODO
        // set https
        // setCookie.setSecure(true);
        response.addCookie(setCookie); // response에 Cookie 추가
        return ResponseEntity.ok(new AuthDto.ResLoginDto(jwtTokenVo.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthDto.ResLogoutDto> logout(@RequestBody AuthDto.ReqLogoutDto reqLogoutDto, HttpServletResponse response) {
        AuthDto.ResLogoutDto resLogoutDto = authService.logout(reqLogoutDto);

        Cookie setCookie = new Cookie("refreshToken", null);
        setCookie.setPath("/");
        setCookie.setMaxAge(0);
        setCookie.setHttpOnly(true);
        response.addCookie(setCookie);
        return ResponseEntity.ok(resLogoutDto);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthDto.ResRefreshTokenDto> refreshToken(@CookieValue(value = "refreshToken") String refreshToken) {
        AuthDto.ResRefreshTokenDto resRefreshTokenDto = null;
        try {
            resRefreshTokenDto = authService.refreshToken(new AuthDto.ReqRefreshTokenDto(refreshToken));
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(resRefreshTokenDto);
    }
}

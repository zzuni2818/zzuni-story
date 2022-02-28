package com.zzuni.zzunistory;

import com.zzuni.zzunistory.config.JwtProvider;
import com.zzuni.zzunistory.domain.Auth;
import com.zzuni.zzunistory.dto.AuthDto;
import com.zzuni.zzunistory.dto.UserDto;
import com.zzuni.zzunistory.service.AuthService;
import com.zzuni.zzunistory.service.UserService;
import com.zzuni.zzunistory.vo.JwtTokenVo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    private static UserDto.ReqSignUpDto reqSignUpDto1;
    private static UserDto.ReqSignUpDto reqSignUpDto2;
    private static UserDto.ReqSignUpDto reqSignUpDto3;

    private static AuthDto.ReqLoginDto reqLoginDto1;
    private static AuthDto.ReqLoginDto reqLoginDto2;
    private static AuthDto.ReqLoginDto reqLoginDto3;

    private static AuthDto.ReqLogoutDto reqLogoutDto1;
    private static AuthDto.ReqLogoutDto reqLogoutDto2;
    private static AuthDto.ReqLogoutDto reqLogoutDto3;

    @BeforeAll
    public static void setUp() {
        reqSignUpDto1 = new UserDto.ReqSignUpDto("User1_username", "User1_password");
        reqSignUpDto2 = new UserDto.ReqSignUpDto("User2_username", "User2_password");
        reqSignUpDto3 = new UserDto.ReqSignUpDto("User3_username", "User3_password");

        reqLoginDto1 = new  AuthDto.ReqLoginDto("User1_username", "User1_password");
        reqLoginDto2 = new  AuthDto.ReqLoginDto("User2_username", "User2_password");
        reqLoginDto3 = new  AuthDto.ReqLoginDto("User3_username", "User3_password");
    }

    @Test
    public void loginAndLogout() {
        /* SETUP tb_user */
        userService.removeAll();
        userService.add(reqSignUpDto1);
        userService.add(reqSignUpDto2);
        userService.add(reqSignUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);

        /* SETUP tb_auth */
        authService.removeAll();
        assertThat(authService.getCount()).isEqualTo(0);

        /* START LOGIN TEST */
        JwtTokenVo jwtTokenVo1 = authService.login(reqLoginDto1);
        assertThat(authService.getCount()).isEqualTo(1);
        String accessToken1 = jwtTokenVo1.getAccessToken();
        Optional<Auth> auth1 = authService.get(jwtProvider.getUserId(accessToken1));
        assertThat(auth1.get().getRefreshToken()).isEqualTo(jwtTokenVo1.getRefreshToken());

        JwtTokenVo jwtTokenVo2 = authService.login(reqLoginDto2);
        assertThat(authService.getCount()).isEqualTo(2);
        String accessToken2 = jwtTokenVo2.getAccessToken();
        Optional<Auth> auth2 = authService.get(jwtProvider.getUserId(accessToken2));
        assertThat(auth2.get().getRefreshToken()).isEqualTo(jwtTokenVo2.getRefreshToken());

        JwtTokenVo jwtTokenVo3 = authService.login(reqLoginDto3);
        assertThat(authService.getCount()).isEqualTo(3);
        String accessToken3 = jwtTokenVo3.getAccessToken();
        Optional<Auth> auth3 = authService.get(jwtProvider.getUserId(accessToken3));
        assertThat(auth3.get().getRefreshToken()).isEqualTo(jwtTokenVo3.getRefreshToken());

        /* SETUP LOGOUT-DTO */
        reqLogoutDto1 = new AuthDto.ReqLogoutDto(jwtProvider.getUserId(accessToken1));
        reqLogoutDto2 = new AuthDto.ReqLogoutDto(jwtProvider.getUserId(accessToken2));
        reqLogoutDto3 = new AuthDto.ReqLogoutDto(jwtProvider.getUserId(accessToken3));

        /* START LOGOUT TEST */
        authService.logout(reqLogoutDto2);
        assertThat(authService.getCount()).isEqualTo(2);
        authService.logout(reqLogoutDto1);
        assertThat(authService.getCount()).isEqualTo(1);
        authService.logout(reqLogoutDto3);
        assertThat(authService.getCount()).isEqualTo(0);
    }

    @Test
    public void refreshToken() {
        /* SETUP tb_user */
        userService.removeAll();
        userService.add(reqSignUpDto1);
        userService.add(reqSignUpDto2);
        userService.add(reqSignUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);

        /* SETUP tb_auth */
        authService.removeAll();
        assertThat(authService.getCount()).isEqualTo(0);

        /* LOGIN FIRST */
        JwtTokenVo jwtTokenVo1 = authService.login(reqLoginDto1);
        assertThat(authService.getCount()).isEqualTo(1);
        String accessToken1 = jwtTokenVo1.getAccessToken();
        Optional<Auth> auth1 = authService.get(jwtProvider.getUserId(accessToken1));
        assertThat(auth1.get().getRefreshToken()).isEqualTo(jwtTokenVo1.getRefreshToken());

        JwtTokenVo jwtTokenVo2 = authService.login(reqLoginDto2);
        assertThat(authService.getCount()).isEqualTo(2);
        String accessToken2 = jwtTokenVo2.getAccessToken();
        Optional<Auth> auth2 = authService.get(jwtProvider.getUserId(accessToken2));
        assertThat(auth2.get().getRefreshToken()).isEqualTo(jwtTokenVo2.getRefreshToken());

        JwtTokenVo jwtTokenVo3 = authService.login(reqLoginDto3);
        assertThat(authService.getCount()).isEqualTo(3);
        String accessToken3 = jwtTokenVo3.getAccessToken();
        Optional<Auth> auth3 = authService.get(jwtProvider.getUserId(accessToken3));
        assertThat(auth3.get().getRefreshToken()).isEqualTo(jwtTokenVo3.getRefreshToken());

        /* SETUP ReqRefreshTokenDto */
        AuthDto.ReqRefreshTokenDto reqRefreshTokenDto1 = new AuthDto.ReqRefreshTokenDto(jwtTokenVo3.getRefreshToken());
        AuthDto.ReqRefreshTokenDto reqRefreshTokenDto2 = new AuthDto.ReqRefreshTokenDto(jwtTokenVo3.getRefreshToken());
        AuthDto.ReqRefreshTokenDto reqRefreshTokenDto3 = new AuthDto.ReqRefreshTokenDto(jwtTokenVo3.getRefreshToken());

        /* START REFRESH TOKEN TEST */
        AuthDto.ResRefreshTokenDto resRefreshTokenDto1 = authService.refreshToken(reqRefreshTokenDto1);
        assertThat(resRefreshTokenDto1.getAccessToken()).isNotEqualTo(accessToken1);
        AuthDto.ResRefreshTokenDto resRefreshTokenDto2 = authService.refreshToken(reqRefreshTokenDto2);
        assertThat(resRefreshTokenDto2.getAccessToken()).isNotEqualTo(accessToken2);
        AuthDto.ResRefreshTokenDto resRefreshTokenDto3 = authService.refreshToken(reqRefreshTokenDto3);
        assertThat(resRefreshTokenDto3.getAccessToken()).isNotEqualTo(accessToken3);
    }
}

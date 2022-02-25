package com.zzuni.zzunistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    public static class ReqSignUpDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class ReqLoginDto {
        private String username;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class ResLoginDto {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class ReqLogoutDto {
        private String username;
    }
}

package com.zzuni.zzunistory.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReqLoginDto {
        private String username;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResLoginDto {
        private String accessToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReqLogoutDto {
        private Long userId;
    }

    @Getter
    @AllArgsConstructor
    public static class ResLogoutDto {
        private boolean success;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReqRefreshTokenDto {
        private String refreshToken;
    }
    @Getter
    @AllArgsConstructor
    public static class ResRefreshTokenDto {
        private String accessToken;
    }
}

package com.zzuni.zzunistory.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    public static class SignUpDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class LoginDto {
        private Long id;
        private String username;
        private String authority;
    }
}

package com.zzuni.zzunistory.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenVo {
    private String accessToken;
    private String refreshToken;
}

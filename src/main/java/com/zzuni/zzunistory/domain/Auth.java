package com.zzuni.zzunistory.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_auth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "refresh_token", unique = true, nullable = false)
    private String refreshToken;

    @Builder
    public Auth(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}

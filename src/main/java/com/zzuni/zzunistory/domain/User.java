package com.zzuni.zzunistory.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authority", nullable = false)
    private String authority; // ROLE_USER, ROLE_ADMIN

    @Builder
    public User(Long id, String username, String password, String authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}

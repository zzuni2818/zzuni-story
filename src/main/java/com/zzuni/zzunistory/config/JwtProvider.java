package com.zzuni.zzunistory.config;

import com.zzuni.zzunistory.domain.User;
import com.zzuni.zzunistory.vo.UserVo;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L; // 1 hour
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L; //1 week

    private String ACCESS_TOKEN_SECRET_KEY = "accessTokenSecretKey_test";
    private String REFRESH_TOKEN_SECRET_KEY = "refreshTokenSecretKey_test";

    public String createAccessToken(UserVo userVo) {
        Claims claims = Jwts.claims().setSubject(userVo.getUsername());
        claims.put("userId", userVo.getId());
        claims.put("roles", userVo.getRoles());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET_KEY)
                .compact();
    }
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET_KEY).parseClaimsJws(accessToken);
        return !claims.getBody().getExpiration().before(new Date());
    }
    public boolean validateRefreshToken(String refreshToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(REFRESH_TOKEN_SECRET_KEY).parseClaimsJws(refreshToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String accessToken) {
      UserVo userVo = new UserVo(User.builder()
              .username(getUsername(accessToken))
              .roles(getRoles(accessToken))
              .build());
      return new UsernamePasswordAuthenticationToken(userVo.getUsername(), "", userVo.getAuthorities());
    }

    private String getUsername(String accessToken) {
        return Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET_KEY).parseClaimsJws(accessToken).getBody().getSubject();
    }
    private String getRoles(String accessToken) {
        return Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET_KEY).parseClaimsJws(accessToken).getBody().get("roles").toString();
    }
}

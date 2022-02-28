package com.zzuni.zzunistory.config;

import com.zzuni.zzunistory.domain.User;
import com.zzuni.zzunistory.vo.UserVo;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    private final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L; // 1 hour
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L; //1 week

    private String SECRET_KEY = "secretKey_test";

    public String createAccessToken(UserVo userVo) {
        Claims claims = Jwts.claims().setSubject(userVo.getUsername());
        claims.put("jti", UUID.randomUUID().toString()); // prevent to create duplicate token
        claims.put("userId", userVo.getId());
        claims.put("roles", userVo.getRoles());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public String createRefreshToken(UserVo userVo) {
        Claims claims = Jwts.claims().setSubject(userVo.getUsername());
        claims.put("userId", userVo.getId());
        claims.put("jti", UUID.randomUUID().toString()); // prevent to create duplicate token
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) throws SignatureException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken);
        return !claims.getBody().getExpiration().before(new Date());
    }
    public boolean validateRefreshToken(String refreshToken) throws SignatureException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(refreshToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String accessToken) {
      UserVo userVo = new UserVo(User.builder()
              .username(getUsername(accessToken))
              .roles(getRoles(accessToken))
              .build());
      return new UsernamePasswordAuthenticationToken(userVo.getUsername(), "", userVo.getAuthorities());
    }

    public Long getUserId(String token) throws SignatureException {
        return Long.parseLong(Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("userId").toString());
    }
    public String getUsername(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
    public String getRoles(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("roles").toString();
    }
    public Date getExpires(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }
}

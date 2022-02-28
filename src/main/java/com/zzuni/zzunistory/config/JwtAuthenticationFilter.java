package com.zzuni.zzunistory.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveAccessToken(request);
        if (accessToken != null) {
            try {
                if (jwtProvider.validateAccessToken(accessToken)) {
                    SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(accessToken));
                } else {
                    String refreshToken = resolveRefreshToken(request);
                    if (refreshToken != null && !jwtProvider.validateRefreshToken(refreshToken)) {
                        response.setStatus(401);
                        return;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            String refreshToken = resolveRefreshToken(request);
            try {
                if (refreshToken != null && !jwtProvider.validateRefreshToken(refreshToken)) {
                    response.setStatus(401);
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for(int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals("refreshToken")) {
                return cookies[i].getValue();
            }
        }
        return null;
    }
}

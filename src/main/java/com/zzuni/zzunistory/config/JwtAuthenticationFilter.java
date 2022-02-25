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
            if (jwtProvider.validateAccessToken(accessToken)) {
                System.out.println("on doFilterInternal, [VALID]:accessToken");
                System.out.println("on doFilterInternal, authentication: " + jwtProvider.getAuthentication(accessToken).getPrincipal().toString());
                SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(accessToken));
            } else {
                String refreshToken = resolveRefreshToken(request);
                if (jwtProvider.validateRefreshToken(refreshToken)) {
                    if (!request.getRequestURI().equals("/auth/refresh_token")) {
                        response.setStatus(401);return;
                    }
                } else {
                    response.setStatus(401);
                    return;
                }
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
        for(int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals("refreshToken")) {
                return cookies[i].getValue();
            }
        }
        return null;
    }
}

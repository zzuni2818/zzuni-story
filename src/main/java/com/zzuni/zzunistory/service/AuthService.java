package com.zzuni.zzunistory.service;

import com.zzuni.zzunistory.config.JwtProvider;
import com.zzuni.zzunistory.domain.Auth;
import com.zzuni.zzunistory.dto.AuthDto;
import com.zzuni.zzunistory.repository.AuthRepository;
import com.zzuni.zzunistory.vo.JwtTokenVo;
import com.zzuni.zzunistory.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(rollbackFor = Exception.class)
    public JwtTokenVo login(AuthDto.ReqLoginDto reqLoginDto) throws UsernameNotFoundException, DataIntegrityViolationException {
        UserVo userVo = userService.loadUserByUsername(reqLoginDto.getUsername());
        if(!passwordEncoder.matches(reqLoginDto.getPassword(), userVo.getPassword())) {
            //TODO
            //Change Exception
            throw new UsernameNotFoundException(reqLoginDto.getUsername());
        }
        String accessToken = jwtProvider.createAccessToken(userVo);
        String refreshToken = jwtProvider.createRefreshToken(userVo);

        authRepository.save(Auth.builder()
                .userId(userVo.getId())
                .refreshToken(refreshToken)
                .build());
        return new JwtTokenVo(accessToken, refreshToken);
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDto.ResLogoutDto logout(AuthDto.ReqLogoutDto reqLogoutDto) {
        authRepository.deleteById(reqLogoutDto.getUserId());
        return new AuthDto.ResLogoutDto(true);
    }

    public AuthDto.ResRefreshTokenDto refreshToken(AuthDto.ReqRefreshTokenDto reqRefreshTokenDto) throws NoSuchElementException {
        Long userId = jwtProvider.getUserId(reqRefreshTokenDto.getRefreshToken());
        System.out.println("on refreshToken, userId: " + userId);
        String refreshTokenOnClient = reqRefreshTokenDto.getRefreshToken();
        String refreshTokenOnServer = get(userId).get().getRefreshToken();
        if(!refreshTokenOnServer.equals(refreshTokenOnClient) || !jwtProvider.validateRefreshToken(refreshTokenOnClient)) {
            //TODO
            // throw
        }
        String accessToken = jwtProvider.createAccessToken(new UserVo(userService.get(userId).get()));
        return new AuthDto.ResRefreshTokenDto(accessToken);
    }

    @Transactional(readOnly = true)
    public Long getCount() {
        return authRepository.count();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        authRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Optional<Auth> get(Long userId) {
        return authRepository.findById(userId);
    }
}

package com.zzuni.zzunistory.service;

import com.zzuni.zzunistory.domain.UserRole;
import com.zzuni.zzunistory.dto.UserDto;
import com.zzuni.zzunistory.domain.User;
import com.zzuni.zzunistory.repository.UserRepository;
import com.zzuni.zzunistory.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        userRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Long getCount() {
        return userRepository.count();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long add(UserDto.ReqSignUpDto reqSignupDto) {
        return userRepository.save(User.builder()
                .username(reqSignupDto.getUsername())
                .password(passwordEncoder.encode(reqSignupDto.getPassword()))
                .roles(UserRole.ROLE_USER.getDescription())
                .build()).getId();
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserVo loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserVo(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}

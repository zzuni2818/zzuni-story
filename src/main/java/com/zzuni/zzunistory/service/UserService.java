package com.zzuni.zzunistory.service;

import com.zzuni.zzunistory.domain.UserRole;
import com.zzuni.zzunistory.dto.UserDto;
import com.zzuni.zzunistory.domain.User;
import com.zzuni.zzunistory.repository.UserRepository;
import com.zzuni.zzunistory.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public Long add(UserDto.SignUpDto signupDto) {
        return userRepository.save(User.builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .authority(UserRole.ROLE_USER.getDescription())
                .build()).getId();
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserVo(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}

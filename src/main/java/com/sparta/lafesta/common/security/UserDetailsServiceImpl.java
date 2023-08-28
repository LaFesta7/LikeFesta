package com.sparta.lafesta.common.security;

import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException((username)));

        return new UserDetailsImpl(user);
    }

    // 회원 탈퇴
    // 비밀번호를 검사 후 , 맞으면 회원 삭제
    // 틀릴 경우 false
    public boolean withdrawl(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        if (passwordEncoder.mathches(password, user.getPassword())) {
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}

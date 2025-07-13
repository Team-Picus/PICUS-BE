package com.picus.core.infrastructure.security.oauth.service;

import com.picus.core.user.application.port.in.UserManagementUseCase;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserManagementUseCase userManagementUseCase;

    @Override
    public UserDetails loadUserByUsername(String userNo) throws UsernameNotFoundException {
        User user = userManagementUseCase.findById(userNo);
        return new org.springframework.security.core.userdetails.User(
                user.getUserNo(),
                "",
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
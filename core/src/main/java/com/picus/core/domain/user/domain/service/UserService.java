package com.picus.core.domain.user.domain.service;

import com.picus.core.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isExist(Long userNo) {
        return userRepository.existsById(userNo);
    }
}

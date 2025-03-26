package com.picus.core.domain.user.domain.service;

import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.domain.user.domain.repository.UserRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isExist(Long userNo) {
        return userRepository.existsById(userNo);
    }

    public User findById(Long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}

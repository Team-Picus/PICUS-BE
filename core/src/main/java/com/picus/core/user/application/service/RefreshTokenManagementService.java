package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.RefreshTokenManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RefreshTokenManagementService implements RefreshTokenManagementUseCase {

    @Override
    public void save(String userNo, String refreshToken, Duration duration) {

    }
}

package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.LoadProfileUseCase;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadProfileService implements LoadProfileUseCase {

    private final UserReadPort userReadPort;

    @Override
    public User load(String userNo) {
        return userReadPort.findById(userNo);
    }
}

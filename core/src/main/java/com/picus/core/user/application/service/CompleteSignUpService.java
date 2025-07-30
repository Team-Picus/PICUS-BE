package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.CompleteSignUpUseCase;
import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
import com.picus.core.user.application.port.out.UserCreatePort;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CompleteSignUpService implements CompleteSignUpUseCase {

    private final UserReadPort userReadPort;
    private final UserCreatePort userCreatePort;

    @Override
    public void complete(CompleteProfileCommand command) {
        User user = userReadPort.findById(command.getUserNo());

        user.update(
                command.getNickname(),
                command.getEmail(),
                command.getTel()
        );

        userCreatePort.create(user);
    }
}

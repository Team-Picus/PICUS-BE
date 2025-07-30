package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.UserManagementUseCase;
import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.ReadUserPort;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UserManagementService implements UserManagementUseCase {

    private final ReadUserPort readUserPort;
    private final UserCommandPort userCommandPort;

    @Override
    public User findById(String userNo) {
        return readUserPort.findById(userNo);
    }

    @Override
    public Role findRoleById(String userNo) {
        return readUserPort.findRoleById(userNo);
    }

    @Override
    public void completeProfile(CompleteProfileCommand command) {
        User user = readUserPort.findById(command.getUserNo());

        user.update(
                command.getNickname(),
                command.getEmail(),
                command.getTel()
        );

        userCommandPort.save(user);
    }
}

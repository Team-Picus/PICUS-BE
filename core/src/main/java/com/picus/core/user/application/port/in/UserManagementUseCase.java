package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;

public interface UserManagementUseCase {

    User findById(String userNo);
    Role findRoleById(String userNo);
    void completeProfile(CompleteProfileCommand command);

}

package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;

import java.util.Optional;

public interface UserQueryPort {

    User findById(String userNo);

    Role findRoleById(String userNo);
}

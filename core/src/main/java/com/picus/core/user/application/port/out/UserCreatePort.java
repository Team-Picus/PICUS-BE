package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.User;

public interface UserCreatePort {

    void create(User user);

}

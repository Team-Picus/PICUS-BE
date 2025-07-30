package com.picus.core.user.application.port.in;

import com.picus.core.user.domain.model.User;

public interface LoadProfileUseCase {

    User load(String userNo);

}

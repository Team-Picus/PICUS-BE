package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;

public interface UserCommandPort {

    User upsert(String providerId, Provider provider, String email, String name, String tel);

}

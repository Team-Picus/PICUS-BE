package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;
import jakarta.validation.constraints.NotBlank;

public interface UserCommandPort {

    User upsert(User user);

    void save(User user);
}

package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.User;

public interface UserCommandPort {

    User upsert(User user);

    void save(User user);

    void assignExpertNo(String userNo, String expertNo);
}

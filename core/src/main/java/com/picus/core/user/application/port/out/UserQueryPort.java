package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.ProfileImage;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;

public interface UserQueryPort {

    User findById(String userNo);

    Role findRoleById(String userNo);

    ProfileImage findProfileImageByExpertNo(String expertNo);
}

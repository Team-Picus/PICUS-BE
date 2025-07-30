package com.picus.core.user.application.port.out;

import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.Role;
import com.picus.core.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserReadPort {

    User findById(String userNo);
    Role findRoleById(String userNo);
    Boolean existsById(String userNo);
    Optional<UserWithProfileImageDto> findUserInfoByExpertNo(String expertNo);
    List<UserWithProfileImageDto> findUserInfoByNicknameContaining(String keyword);
    List<UserWithProfileImageDto> findTopNUserInfoByNicknameContainingOrderByNickname(String keyword, int size);
}

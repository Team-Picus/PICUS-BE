package com.picus.core.follow.application.port.in;

import com.picus.core.follow.domain.model.Follow;

import java.util.List;

public interface FollowManagementUseCase {

    void follow(String userNo, String to);
    void unfollow(String userNo, String expertNo);
    List<Follow> getFollows(String userNo);

}

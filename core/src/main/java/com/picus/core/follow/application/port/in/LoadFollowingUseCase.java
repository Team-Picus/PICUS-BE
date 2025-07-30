package com.picus.core.follow.application.port.in;

import com.picus.core.follow.domain.Follow;

import java.util.List;

public interface LoadFollowingUseCase {

    List<Follow> getFollows(String userNo);

}

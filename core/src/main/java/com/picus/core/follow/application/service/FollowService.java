package com.picus.core.follow.application.service;

import com.picus.core.follow.application.port.in.FollowUseCase;
import com.picus.core.follow.application.port.in.LoadFollowingUseCase;
import com.picus.core.follow.application.port.in.UnfollowUseCase;
import com.picus.core.follow.application.port.out.FollowCreatePort;
import com.picus.core.follow.application.port.out.FollowDeletePort;
import com.picus.core.follow.application.port.out.FollowReadPort;
import com.picus.core.follow.domain.Follow;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._EXIST_ENTITY;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class FollowService
        implements FollowUseCase, UnfollowUseCase, LoadFollowingUseCase {

    private final FollowReadPort followReadPort;
    private final UserReadPort userReadPort;
    private final FollowCreatePort followCreatePort;
    private final FollowDeletePort followDeletePort;

    @Override
    public void follow(String userNo, String expertNo) {
        if (followReadPort.existsById(userNo, expertNo))
            throw new RestApiException(_EXIST_ENTITY);

        if(!userReadPort.existsById(userNo) /* && readExpertPort.existsById(expertNo)*/)
            throw new RestApiException(_NOT_FOUND);

        followCreatePort.create(userNo, expertNo);
    }

    @Override
    public void unfollow(String userNo, String expertNo) {
        if (!followReadPort.existsById(userNo, expertNo))
            throw new RestApiException(_NOT_FOUND);

        followDeletePort.delete(userNo, expertNo);
    }

    @Override
    public List<Follow> getFollows(String userNo) {
        return followReadPort.findByUserNo(userNo);
    }
}

package com.picus.core.follow.application.service;

import com.picus.core.follow.application.port.in.FollowManagementUseCase;
import com.picus.core.follow.application.port.out.FollowCommandPort;
import com.picus.core.follow.application.port.out.FollowQueryPort;
import com.picus.core.follow.domain.model.Follow;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.ReadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._EXIST_ENTITY;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class FollowManagementService implements FollowManagementUseCase {

    private final FollowQueryPort followQueryPort;
    private final ReadUserPort readUserPort;
    private final FollowCommandPort followCommandPort;

    @Override
    public void follow(String userNo, String expertNo) {
        if (followQueryPort.existsById(userNo, expertNo))
            throw new RestApiException(_EXIST_ENTITY);

        if(!readUserPort.existsById(userNo) /* && expertQueryPort.existsById(expertNo)*/)
            throw new RestApiException(_NOT_FOUND);

        followCommandPort.save(userNo, expertNo);
    }

    @Override
    public void unfollow(String userNo, String expertNo) {
        if (!followQueryPort.existsById(userNo, expertNo))
            throw new RestApiException(_NOT_FOUND);

        followCommandPort.delete(userNo, expertNo);
    }

    @Override
    public List<Follow> getFollows(String userNo) {
        return followQueryPort.findByUserNo(userNo);
    }
}

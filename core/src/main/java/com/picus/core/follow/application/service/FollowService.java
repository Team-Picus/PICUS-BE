package com.picus.core.follow.application.service;

import com.picus.core.follow.application.port.in.FollowUseCase;
import com.picus.core.follow.application.port.in.LoadFollowingUseCase;
import com.picus.core.follow.application.port.in.UnfollowUseCase;
import com.picus.core.follow.application.port.out.CreateFollowPort;
import com.picus.core.follow.application.port.out.DeleteFollowPort;
import com.picus.core.follow.application.port.out.ReadFollowPort;
import com.picus.core.follow.domain.Follow;
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
public class FollowService
        implements FollowUseCase, UnfollowUseCase, LoadFollowingUseCase {

    private final ReadFollowPort readFollowPort;
    private final ReadUserPort readUserPort;
    private final CreateFollowPort createFollowPort;
    private final DeleteFollowPort deleteFollowPort;

    @Override
    public void follow(String userNo, String expertNo) {
        if (readFollowPort.existsById(userNo, expertNo))
            throw new RestApiException(_EXIST_ENTITY);

        if(!readUserPort.existsById(userNo) /* && readExpertPort.existsById(expertNo)*/)
            throw new RestApiException(_NOT_FOUND);

        createFollowPort.create(userNo, expertNo);
    }

    @Override
    public void unfollow(String userNo, String expertNo) {
        if (!readFollowPort.existsById(userNo, expertNo))
            throw new RestApiException(_NOT_FOUND);

        deleteFollowPort.delete(userNo, expertNo);
    }

    @Override
    public List<Follow> getFollows(String userNo) {
        return readFollowPort.findByUserNo(userNo);
    }
}

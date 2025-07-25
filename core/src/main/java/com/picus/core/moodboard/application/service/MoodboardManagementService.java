package com.picus.core.moodboard.application.service;

import com.picus.core.moodboard.application.port.in.MoodboardManagementUseCase;
import com.picus.core.moodboard.application.port.out.MoodboardCommandPort;
import com.picus.core.moodboard.application.port.out.MoodboardQueryPort;
import com.picus.core.moodboard.domain.model.Moodboard;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class MoodboardManagementService implements MoodboardManagementUseCase {

    private final UserQueryPort userQueryPort;
    // private final PostQueryPort postQueryPort;
    private final MoodboardCommandPort moodboardCommandPort;
    private final MoodboardQueryPort moodboardQueryPort;

    @Override
    public void save(String userNo, String postNo) {
        if (!userQueryPort.existsById(userNo) /* || !postQueryPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        moodboardCommandPort.save(userNo, postNo);
    }

    @Override
    public void delete(String userNo, String postNo) {
        if (!userQueryPort.existsById(userNo) /* || !postQueryPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        if(!moodboardQueryPort.existsById(userNo, postNo))
            throw new RestApiException(_NOT_FOUND);

        moodboardCommandPort.delete(userNo, postNo);
    }

    @Override
    public List<Moodboard> getMoodboards(String userNo) {
        return moodboardQueryPort.findByUserNo(userNo);
    }
}

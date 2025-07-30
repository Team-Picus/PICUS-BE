package com.picus.core.moodboard.application.service;

import com.picus.core.moodboard.application.port.in.CreateMoodboardUseCase;
import com.picus.core.moodboard.application.port.in.LoadMoodboardUseCase;
import com.picus.core.moodboard.application.port.in.MoodboardManagementUseCase;
import com.picus.core.moodboard.application.port.out.MoodboardCreatePort;
import com.picus.core.moodboard.application.port.out.MoodboardDeletePort;
import com.picus.core.moodboard.application.port.out.MoodboardReadPort;
import com.picus.core.moodboard.domain.Moodboard;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class MoodboardManagementService 
        implements MoodboardManagementUseCase, CreateMoodboardUseCase, LoadMoodboardUseCase {

    private final UserReadPort userReadPort;
    private final MoodboardCreatePort moodboardCreatePort;
    private final MoodboardDeletePort moodboardDeletePort;
    private final MoodboardReadPort moodboardReadPort;

    @Override
    public void create(String userNo, String postNo) {
        if (!userReadPort.existsById(userNo) /* || !readPostPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        moodboardCreatePort.create(userNo, postNo);
    }

    @Override
    public void delete(String userNo, String postNo) {
        if (!userReadPort.existsById(userNo) /* || !readPostPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        if(!moodboardReadPort.existsById(userNo, postNo))
            throw new RestApiException(_NOT_FOUND);

        moodboardDeletePort.delete(userNo, postNo);
    }

    @Override
    public List<Moodboard> loadAll(String userNo) {
        return moodboardReadPort.findByUserNo(userNo);
    }
}

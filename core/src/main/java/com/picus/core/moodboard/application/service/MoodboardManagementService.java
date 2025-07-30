package com.picus.core.moodboard.application.service;

import com.picus.core.moodboard.application.port.in.CreateMoodboardUseCase;
import com.picus.core.moodboard.application.port.in.LoadMoodboardUseCase;
import com.picus.core.moodboard.application.port.in.MoodboardManagementUseCase;
import com.picus.core.moodboard.application.port.out.CreateMoodboardPort;
import com.picus.core.moodboard.application.port.out.DeleteMoodboardPort;
import com.picus.core.moodboard.application.port.out.ReadMoodboardPort;
import com.picus.core.moodboard.domain.Moodboard;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.ReadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class MoodboardManagementService 
        implements MoodboardManagementUseCase, CreateMoodboardUseCase, LoadMoodboardUseCase {

    private final ReadUserPort readUserPort;
    private final CreateMoodboardPort createMoodboardPort;
    private final DeleteMoodboardPort deleteMoodboardPort;
    private final ReadMoodboardPort readMoodboardPort;

    @Override
    public void create(String userNo, String postNo) {
        if (!readUserPort.existsById(userNo) /* || !readPostPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        createMoodboardPort.create(userNo, postNo);
    }

    @Override
    public void delete(String userNo, String postNo) {
        if (!readUserPort.existsById(userNo) /* || !readPostPort.existsById(postNo) */)
            throw new RestApiException(_NOT_FOUND);

        if(!readMoodboardPort.existsById(userNo, postNo))
            throw new RestApiException(_NOT_FOUND);

        deleteMoodboardPort.delete(userNo, postNo);
    }

    @Override
    public List<Moodboard> loadAll(String userNo) {
        return readMoodboardPort.findByUserNo(userNo);
    }
}

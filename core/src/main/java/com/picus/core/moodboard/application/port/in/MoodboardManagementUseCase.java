package com.picus.core.moodboard.application.port.in;

import com.picus.core.moodboard.domain.model.Moodboard;

import java.util.List;

public interface MoodboardManagementUseCase {

    void save(String userNo, String postNo);
    void delete(String userNo, String postNo);
    List<Moodboard> findByUserNo(String userNo);

}

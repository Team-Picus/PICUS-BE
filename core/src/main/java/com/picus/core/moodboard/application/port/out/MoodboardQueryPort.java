package com.picus.core.moodboard.application.port.out;

import com.picus.core.moodboard.domain.model.Moodboard;

import java.util.List;

public interface MoodboardQueryPort {

    boolean existsById(String userNo, String postNo);

    List<Moodboard> findByUserNo(String userNo);
}

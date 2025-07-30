package com.picus.core.moodboard.application.port.out;

import com.picus.core.moodboard.domain.Moodboard;

import java.util.List;

public interface MoodboardReadPort {

    boolean existsById(String userNo, String postNo);
    List<Moodboard> findByUserNo(String userNo);

}

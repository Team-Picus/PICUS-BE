package com.picus.core.moodboard.application.port.in;

import com.picus.core.moodboard.domain.Moodboard;

import java.util.List;

public interface LoadMoodboardUseCase {

    List<Moodboard> loadAll(String userNo);

}

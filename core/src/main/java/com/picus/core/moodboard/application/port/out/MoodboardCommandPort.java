package com.picus.core.moodboard.application.port.out;

public interface MoodboardCommandPort {

    void save(String userNo, String postNo);

    void delete(String userNo, String postNo);
}

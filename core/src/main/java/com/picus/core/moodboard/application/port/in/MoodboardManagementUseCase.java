package com.picus.core.moodboard.application.port.in;

public interface MoodboardManagementUseCase {

    void save(String userNo, String postNo);
    void delete(String userNo, String postNo);

}

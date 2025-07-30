package com.picus.core.moodboard.adapter.in.web.mapper;

import com.picus.core.moodboard.adapter.in.web.data.response.LoadMoodboardResponse;
import com.picus.core.moodboard.domain.Moodboard;
import org.springframework.stereotype.Component;

@Component
public class MoodboardWebMapper {

    public LoadMoodboardResponse toResponse(Moodboard moodboard) {
        return LoadMoodboardResponse.builder()
                .userNo(moodboard.getUserNo())
                .postNo(moodboard.getPostNo())
                .createdAt(moodboard.getCreatedAt())
                .build();
    }
}

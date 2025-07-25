package com.picus.core.moodboard.adapter.in.web.mapper;

import com.picus.core.moodboard.adapter.in.web.data.response.GetMoodboardResponse;
import com.picus.core.moodboard.domain.model.Moodboard;
import org.springframework.stereotype.Component;

@Component
public class MoodboardWebMapper {

    public GetMoodboardResponse toResponse(Moodboard moodboard) {
        return GetMoodboardResponse.builder()
                .userNo(moodboard.getUserNo())
                .postNo(moodboard.getPostNo())
                .createdAt(moodboard.getCreatedAt())
                .build();
    }
}

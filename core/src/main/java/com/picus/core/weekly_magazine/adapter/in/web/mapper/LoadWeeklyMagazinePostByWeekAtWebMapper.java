package com.picus.core.weekly_magazine.adapter.in.web.mapper;

import com.picus.core.weekly_magazine.adapter.in.web.data.response.LoadWeeklyMagazinePostByWeekAtResponse;
import com.picus.core.weekly_magazine.application.port.in.result.LoadWeeklyMagazinePostByWeekAtResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadWeeklyMagazinePostByWeekAtWebMapper {

    public LoadWeeklyMagazinePostByWeekAtResponse toResponse(List<LoadWeeklyMagazinePostByWeekAtResult> results) {
        return LoadWeeklyMagazinePostByWeekAtResponse.builder()
                .posts(toPostResponses(results))
                .build();
    }

    private static List<LoadWeeklyMagazinePostByWeekAtResponse.PostResponse> toPostResponses(List<LoadWeeklyMagazinePostByWeekAtResult> results) {
        return results.stream()
                .map(result -> LoadWeeklyMagazinePostByWeekAtResponse.PostResponse.builder()
                        .postNo(result.postNo())
                        .authorNickname(result.authorNickname())
                        .postTitle(result.postTitle())
                        .thumbnailUrl(result.thumbnailUrl())
                        .build()
                ).toList();
    }

}

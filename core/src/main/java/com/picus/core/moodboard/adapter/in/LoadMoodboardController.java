package com.picus.core.moodboard.adapter.in;

import com.picus.core.moodboard.adapter.in.web.data.response.LoadMoodboardResponse;
import com.picus.core.moodboard.adapter.in.web.mapper.MoodboardWebMapper;
import com.picus.core.moodboard.application.port.in.LoadMoodboardUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moodboards")
public class LoadMoodboardController {

    private final LoadMoodboardUseCase loadMoodboardUseCase;
    private final MoodboardWebMapper moodboardWebMapper;

    @GetMapping
    public BaseResponse<List<LoadMoodboardResponse>> getMoodboards(
            @CurrentUser String userNo
    ) {
        List<LoadMoodboardResponse> response = loadMoodboardUseCase.loadAll(userNo).stream()
                .map(moodboardWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}

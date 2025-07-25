package com.picus.core.moodboard.adapter.in;

import com.picus.core.moodboard.adapter.in.web.data.response.GetMoodboardResponse;
import com.picus.core.moodboard.adapter.in.web.mapper.MoodboardWebMapper;
import com.picus.core.moodboard.application.port.in.MoodboardManagementUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moodboards")
public class MoodboardController {

    private final MoodboardManagementUseCase moodboardManagementUseCase;
    private final MoodboardWebMapper moodboardWebMapper;

    @PostMapping
    public BaseResponse<Void> save(@CurrentUser String userNo, @RequestParam String postNo) {
        moodboardManagementUseCase.save(userNo, postNo);
        return BaseResponse.onSuccess();
    }

    @DeleteMapping
    public BaseResponse<Void> delete(@CurrentUser String userNo, @RequestParam String postNo) {
        moodboardManagementUseCase.delete(userNo, postNo);
        return BaseResponse.onSuccess();
    }

    @GetMapping
    public BaseResponse<List<GetMoodboardResponse>> getMoodboards(@CurrentUser String userNo) {
        List<GetMoodboardResponse> response = moodboardManagementUseCase.getMoodboards(userNo).stream()
                .map(moodboardWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(response);
    }
}

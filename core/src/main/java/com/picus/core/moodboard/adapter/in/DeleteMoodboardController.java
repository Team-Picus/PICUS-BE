package com.picus.core.moodboard.adapter.in;

import com.picus.core.moodboard.application.port.in.MoodboardManagementUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moodboards")
public class DeleteMoodboardController {

    private final MoodboardManagementUseCase moodboardManagementUseCase;

    @DeleteMapping
    public BaseResponse<Void> delete(
            @CurrentUser String userNo,
            @RequestParam String postNo
    ) {
        moodboardManagementUseCase.delete(userNo, postNo);
        return BaseResponse.onSuccess();
    }
}

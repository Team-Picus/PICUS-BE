package com.picus.core.moodboard.adapter.in;

import com.picus.core.moodboard.application.port.in.CreateMoodboardUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/moodboards")
public class CreateMoodboardController {

    private final CreateMoodboardUseCase createMoodboardUseCase;

    @PostMapping
    public BaseResponse<Void> create(
            @CurrentUser String userNo,
            @RequestParam String postNo
    ) {
        createMoodboardUseCase.create(userNo, postNo);
        return BaseResponse.onSuccess();
    }
}

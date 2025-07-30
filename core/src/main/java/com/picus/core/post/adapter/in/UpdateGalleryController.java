package com.picus.core.post.adapter.in;

import com.picus.core.post.application.port.in.UpdateGalleryUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/experts")
public class UpdateGalleryController {

    private final UpdateGalleryUseCase updateGalleryUseCase;

    @PatchMapping("/posts/{post_no}/gallery")
    public BaseResponse<Void> updateGallery(@PathVariable("post_no") String postNo, @CurrentUser String userNo) {
        updateGalleryUseCase.update(postNo, userNo);
        return BaseResponse.onSuccess();
    }
}

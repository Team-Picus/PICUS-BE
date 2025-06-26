package com.picus.core.old.domain.shared.image.ui;

import com.picus.core.old.domain.shared.image.application.usecase.ImageUploadUseCase;
import com.picus.core.old.global.common.base.BaseResponse;
import com.picus.core.old.domain.shared.image.application.dto.request.UploadImage;
import com.picus.core.old.domain.shared.image.application.dto.response.UploadUrl;
import com.picus.core.old.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    private final ImageUploadUseCase imageUploadUseCase;

    /***
     * 이미지 및 파일 업로드를 위한 pre-signed URL 발급 API
     * @param userPrincipal
     * @param filename
     * @return
     */
    @GetMapping("/upload")
    public BaseResponse<UploadUrl> requestUploadURL(@CommonPrincipal UserPrincipal userPrincipal, @RequestParam UploadImage request) {
        return BaseResponse.onSuccess(imageUploadUseCase.getPostS3Url(userPrincipal.getUserId(), request));
    }
}

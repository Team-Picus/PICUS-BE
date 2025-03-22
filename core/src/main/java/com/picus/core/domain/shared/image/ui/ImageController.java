package com.picus.core.domain.shared.image.ui;

import com.picus.core.domain.shared.image.application.dto.response.ImageUrl;
import com.picus.core.domain.shared.image.application.usecase.ImageUseCase;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageUseCase imageUseCase;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ImageUrl>> findPostImages(@CommonPrincipal UserPrincipal userPrincipal,
                                                         Long postId) {
        List<ImageUrl> postImages = imageUseCase.findPostImages(postId);
        return ResponseEntity.ok(postImages);
    }
}

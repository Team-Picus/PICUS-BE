package com.picus.core.global.common.image.domain.factory.strategy;

import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewImageCreationStrategy implements ImageCreationStrategy {

    private static final ReviewImageCreationStrategy INSTANCE = new ReviewImageCreationStrategy();

    public static ReviewImageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Image toEntity(String key, UploadImage request) {
        return null;
    }
}

package com.picus.core.global.common.image.domain.factory.strategy;

import com.picus.core.domain.post.domain.entity.image.PostImage;
import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImageCreationStrategy implements ImageCreationStrategy {

    private static final PostImageCreationStrategy INSTANCE = new PostImageCreationStrategy();

    public static PostImageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Image toEntity(String key, UploadImage request) {
        return new PostImage(key, request.postNo());
    }
}

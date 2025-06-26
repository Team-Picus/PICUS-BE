package com.picus.core.old.domain.shared.image.domain.factory.strategy;

import com.picus.core.old.domain.post.domain.entity.image.PostImageResource;
import com.picus.core.old.domain.shared.image.application.dto.request.UploadImage;
import com.picus.core.old.domain.shared.image.domain.entity.Image;
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
        return new PostImageResource(key, request.postNo());
    }
}

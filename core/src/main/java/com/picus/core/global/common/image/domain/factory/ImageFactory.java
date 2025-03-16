package com.picus.core.global.common.image.domain.factory;

import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.common.image.domain.factory.strategy.ImageCreationStrategy;
import com.picus.core.global.common.image.domain.factory.strategy.MessageImageCreationStrategy;
import com.picus.core.global.common.image.domain.factory.strategy.PostImageCreationStrategy;
import com.picus.core.global.common.image.domain.factory.strategy.ReviewImageCreationStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImageFactory {

    private final Map<ImageType, ImageCreationStrategy> creationStrategies;

    private ImageFactory() {
        this.creationStrategies = Map.of(
                ImageType.MESSAGE, MessageImageCreationStrategy.getInstance(),
                ImageType.POST, PostImageCreationStrategy.getInstance(),
                ImageType.REVIEW, ReviewImageCreationStrategy.getInstance()
        );
    }

    public Image toEntity(String key, UploadImage request) {
        return creationStrategies.get(request.imageType())
                .toEntity(key, request);
    }
}

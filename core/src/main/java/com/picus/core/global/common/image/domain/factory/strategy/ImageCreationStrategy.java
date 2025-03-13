package com.picus.core.global.common.image.domain.factory.strategy;

import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;

public interface ImageCreationStrategy {
    Image toEntity(String key, UploadImage request);
}

package com.picus.core.old.domain.shared.image.domain.factory.strategy;

import com.picus.core.old.domain.shared.image.application.dto.request.UploadImage;
import com.picus.core.old.domain.shared.image.domain.entity.Image;

public interface ImageCreationStrategy {
    Image toEntity(String key, UploadImage request);
}

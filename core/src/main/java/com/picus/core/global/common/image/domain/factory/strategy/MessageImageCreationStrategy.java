package com.picus.core.global.common.image.domain.factory.strategy;

import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;
import com.picus.core.global.common.image.domain.entity.MessageImageResource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageImageCreationStrategy implements ImageCreationStrategy {

    private static final MessageImageCreationStrategy INSTANCE = new MessageImageCreationStrategy();

    public static MessageImageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Image toEntity(String key, UploadImage request) {
        return new MessageImageResource(key);
    }
}

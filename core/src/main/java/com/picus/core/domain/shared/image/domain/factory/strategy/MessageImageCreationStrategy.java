package com.picus.core.domain.shared.image.domain.factory.strategy;

import com.picus.core.domain.shared.image.application.dto.request.UploadImage;
import com.picus.core.domain.shared.image.domain.entity.Image;
import com.picus.core.domain.chat.domain.entity.message.MessageImageResource;
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

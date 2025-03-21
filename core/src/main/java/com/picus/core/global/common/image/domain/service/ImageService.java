package com.picus.core.global.common.image.domain.service;

import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.domain.entity.Image;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.common.image.domain.factory.ImageFactory;
import com.picus.core.global.common.image.domain.repository.ImageRepository;
import com.picus.core.global.common.image.domain.repository.MessageImageResourceRepository;
import com.picus.core.global.common.image.domain.repository.PostImageResourceRepository;
import com.picus.core.global.common.image.domain.repository.ReviewImageResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._METHOD_ARGUMENT_ERROR;
import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageFactory imageFactory;
    private final MessageImageResourceRepository messageImageResourceRepository;
    private final PostImageResourceRepository postImageResourceRepository;
    private final ReviewImageResourceRepository reviewImageResourceRepository;

    public Image save(String key, UploadImage request) {
        return imageRepository.save(imageFactory.toEntity(key, request));
    }

    public Image findImage(Long imageId, ImageType imageType) {
        return switch (imageType) {
            case BACKGROUND -> null;    // todo: 새 로직 생성
            case PROFILE -> null;
            case MESSAGE -> messageImageResourceRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            case POST -> postImageResourceRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            case REVIEW -> reviewImageResourceRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            case null -> throw new RestApiException(_METHOD_ARGUMENT_ERROR);
        };
    }
}

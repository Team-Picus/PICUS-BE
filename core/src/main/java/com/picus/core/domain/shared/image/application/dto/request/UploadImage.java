package com.picus.core.domain.shared.image.application.dto.request;

import com.picus.core.domain.shared.image.domain.entity.ImageType;

public record UploadImage (
        String filename,
        ImageType imageType,
        Long postNo,
        Long reviewNo
) {}

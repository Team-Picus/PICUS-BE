package com.picus.core.global.common.image.application.dto.request;

import com.picus.core.global.common.image.domain.entity.ImageType;

public record UploadImage (
        String filename,
        ImageType imageType,
        Long postNo,
        Long reviewNo
) {}

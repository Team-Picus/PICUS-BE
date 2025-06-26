package com.picus.core.old.domain.shared.image.application.dto.request;

import com.picus.core.old.domain.shared.image.domain.entity.ImageType;

public record UploadImage (
        String filename,
        ImageType imageType,
        Long postNo,
        Long reviewNo
) {}

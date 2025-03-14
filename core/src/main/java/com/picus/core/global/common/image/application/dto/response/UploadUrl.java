package com.picus.core.global.common.image.application.dto.response;

import lombok.Builder;

@Builder
public record UploadUrl (
        String preSignedUrl,
        Long imageId
) {}

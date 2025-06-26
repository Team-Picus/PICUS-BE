package com.picus.core.old.domain.shared.image.application.dto.response;

import lombok.Builder;

@Builder
public record UploadUrl (
        String preSignedUrl,
        Long imageId
) {}

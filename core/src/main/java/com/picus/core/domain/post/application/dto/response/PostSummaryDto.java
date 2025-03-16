package com.picus.core.domain.post.application.dto.response;

import java.util.List;

public record PostSummaryDto(Long id,
                             Long studioNo,
                             String title,
                             String detail,
                             Integer basicPrice
//                             List<PostImageDto> imageKeys
) {
}

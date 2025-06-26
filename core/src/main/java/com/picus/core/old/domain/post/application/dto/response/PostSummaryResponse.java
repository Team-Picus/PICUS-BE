package com.picus.core.old.domain.post.application.dto.response;

public record PostSummaryResponse(Long id,
                                  Long studioNo,
                                  String title,
                                  String detail,
                                  Integer basicPrice
//                             List<PostImageDto> imageKeys
) {
}

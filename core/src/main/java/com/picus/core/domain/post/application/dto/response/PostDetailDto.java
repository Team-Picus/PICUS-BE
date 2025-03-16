package com.picus.core.domain.post.application.dto.response;

import com.picus.core.domain.post.domain.entity.PostStatus;
import com.picus.core.global.common.enums.ApprovalStatus;

import java.util.List;

public record PostDetailDto(
        Long id,
        String title,
        String detail,
        Long studioNo,
        List<DistrictDto> availableAreas,  // District의 displayName 또는 name을 매핑
        BasicOptionDto basicOption,
//        List<PostImageDto> images,
        List<CategoryDto> categories,
        PostStatus postStatus,
        ApprovalStatus approvalStatus
) {}
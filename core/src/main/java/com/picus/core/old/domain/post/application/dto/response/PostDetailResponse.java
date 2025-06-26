package com.picus.core.old.domain.post.application.dto.response;

import com.picus.core.old.domain.post.domain.entity.PostStatus;
import com.picus.core.old.domain.shared.approval.ApprovalStatus;
import com.picus.core.old.domain.shared.image.application.dto.response.ImageUrl;

import java.util.List;

public record PostDetailResponse(
        Long id,
        String title,
        String detail,
        Long studioNo,
        List<DistrictResponse> availableAreas,  // District의 displayName 또는 name을 매핑
        BasicOptionResponse basicOption,
        List<CategoryRespnose> categories,
        PostStatus postStatus,
        List<ImageUrl> imageUrls,
        ApprovalStatus approvalStatus
) {}
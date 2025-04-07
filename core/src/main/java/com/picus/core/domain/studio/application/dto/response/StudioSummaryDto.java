package com.picus.core.domain.studio.application.dto.response;

import com.picus.core.domain.shared.approval.ApprovalStatus;
import com.picus.core.domain.studio.domain.entity.Address;

import java.time.LocalDateTime;

public record StudioSummaryDto(
        Long studioNo,
        String name,
        Long backgroundImgUrl,
        Address address,
        LocalDateTime recentActiveAt,
        Long expertNo,
        ApprovalStatus approvalStatus
) {}
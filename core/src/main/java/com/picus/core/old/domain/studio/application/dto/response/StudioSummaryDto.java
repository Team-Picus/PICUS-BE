package com.picus.core.old.domain.studio.application.dto.response;

import com.picus.core.old.domain.shared.approval.ApprovalStatus;
import com.picus.core.old.domain.studio.domain.entity.Address;

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
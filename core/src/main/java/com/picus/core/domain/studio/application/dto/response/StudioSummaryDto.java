package com.picus.core.domain.studio.application.dto.response;

import com.picus.core.domain.studio.domain.entity.Address;
import com.picus.core.global.common.enums.ApprovalStatus;

import java.time.LocalDateTime;

public record StudioSummaryDto(
        Long studioNo,
        String name,
        String backgroundImgUrl,
        Address address,
        LocalDateTime recentActiveAt,
        Long expertNo,
        ApprovalStatus approvalStatus
) {}
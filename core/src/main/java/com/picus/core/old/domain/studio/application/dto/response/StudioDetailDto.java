package com.picus.core.old.domain.studio.application.dto.response;


public record StudioDetailDto(StudioSummaryDto summary,
                              // 통계성
                              Integer reviewCount,
                              Integer activityCount,
                              Double avgRating) {
}

package com.picus.core.domain.studio.application.dto.response;


public record StudioDetailDto(StudioSummaryDto summary,
                              // 통계성
                              Integer reviewCount,
                              Integer activityCount,
                              Double avgRating) {
}

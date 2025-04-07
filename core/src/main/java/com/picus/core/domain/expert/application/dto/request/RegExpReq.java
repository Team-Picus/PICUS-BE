package com.picus.core.domain.expert.application.dto.request;

import com.picus.core.domain.shared.area.District;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RegExpReq(
    @NotEmpty String intro,
    @NotEmpty String career,
    @NotEmpty Set<String> skills,
    @NotEmpty Set<District> activityAreas
) {}

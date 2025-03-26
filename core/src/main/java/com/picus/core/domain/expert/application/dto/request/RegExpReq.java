package com.picus.core.domain.expert.application.dto.request;

import java.util.Set;

public record RegExpReq(
    String intro,
    String career,
    Set<String> skills,
    Set<String> activityAreas
) {}

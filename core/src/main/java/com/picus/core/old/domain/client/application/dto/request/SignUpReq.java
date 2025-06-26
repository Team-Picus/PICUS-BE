package com.picus.core.old.domain.client.application.dto.request;

import com.picus.core.old.domain.shared.area.District;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record SignUpReq (
        @NotEmpty String nickname,
        Long profileImgId,
        @NotEmpty Set<District> preferredAreas
) {}

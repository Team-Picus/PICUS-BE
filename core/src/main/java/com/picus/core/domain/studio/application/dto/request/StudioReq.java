package com.picus.core.domain.studio.application.dto.request;

import com.picus.core.domain.studio.domain.entity.Address;

public record StudioReq (
        String name,
        Long backgroundImgId,
        Address address
) {}

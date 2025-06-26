package com.picus.core.old.domain.studio.application.dto.request;

import com.picus.core.old.domain.studio.domain.entity.Address;

public record StudioReq (
        String name,
        Long backgroundImgId,
        Address address
) {}

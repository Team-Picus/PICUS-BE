package com.picus.core.domain.client.entity.area;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientDistrictId implements Serializable {

    @EqualsAndHashCode.Include
    private Long client;

    @EqualsAndHashCode.Include
    private Long district;

    public ClientDistrictId(Long client, Long district) {
        this.client = client;
        this.district = district;
    }
}

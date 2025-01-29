package com.picus.core.domain.expert.entity.area;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExpertDistrictId implements Serializable {

    @EqualsAndHashCode.Include
    private Long expert;

    @EqualsAndHashCode.Include
    private Long district;

    public ExpertDistrictId(Long expert, Long district) {
        this.expert = expert;
        this.district = district;
    }
}

package com.picus.core.domain.expert.domain.entity.area;

import com.picus.core.global.common.area.entity.District;
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
    private District district;

    public ExpertDistrictId(Long expertNo, District district) {
        this.expert = expertNo;
        this.district = district;
    }
}

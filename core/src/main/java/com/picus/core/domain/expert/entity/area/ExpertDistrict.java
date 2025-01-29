package com.picus.core.domain.expert.entity.area;

import com.picus.core.domain.expert.entity.Expert;
import com.picus.core.global.common.area.entity.District;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(ExpertDistrictId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertDistrict {

    @Id
    @ManyToOne
    @JoinColumn(name = "expert_no", nullable = false)
    private Expert expert;

    @Id
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    public ExpertDistrict(Expert expert, District district) {
        this.expert = expert;
        this.district = district;
    }
}

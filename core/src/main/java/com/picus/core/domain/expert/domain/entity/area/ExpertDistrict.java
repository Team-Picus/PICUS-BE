package com.picus.core.domain.expert.domain.entity.area;

import com.picus.core.domain.expert.domain.entity.Expert;
import com.picus.core.domain.shared.area.domain.entity.District;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@IdClass(ExpertDistrictId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertDistrict extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "expert_no", nullable = false)
    private Expert expert;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "district", nullable = false)
    private District district;

    public ExpertDistrict(Expert expert, District district) {
        this.expert = expert;
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpertDistrict that = (ExpertDistrict) o;
        return Objects.equals(expert, that.expert) && Objects.equals(district, that.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expert, district);
    }
}

package com.picus.core.domain.client.domain.entity;

import com.picus.core.domain.shared.area.District;
import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.global.common.converter.DistrictSetConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity {

    @Id
    @Column(name = "client_no")
    private Long id;

    @Convert(converter = DistrictSetConverter.class)
    private Set<District> preferredAreas = new HashSet<>();

    public void updatePreferredArea(Set<District> areas) {
        this.preferredAreas.addAll(areas);
    }

    public Client(Long id, Set<District> preferredAreas) {
        this.id = id;
        this.preferredAreas = preferredAreas;
    }
}

package com.picus.core.domain.client.entity;

import com.picus.core.domain.client.entity.area.ClientDistrict;
import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity {

    @Id
    @Column(name = "client_no")
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClientDistrict> preferredAreas = new ArrayList<>();

    public void updatePreferredArea(List<ClientDistrict> areas) {
        for (ClientDistrict area : areas) {
            if (!this.preferredAreas.contains(area)) {
                this.preferredAreas.add(area);
            }
        }

        this.preferredAreas.removeIf(existingArea -> !areas.contains(existingArea));
    }
}

package com.picus.core.domain.client.domain.entity;

import com.picus.core.domain.client.domain.entity.area.ClientDistrict;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity {

    @Id
    @Column(name = "client_no")
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "client")
    private Set<ClientDistrict> preferredAreas = new HashSet<>();

    public void updatePreferredArea(Set<ClientDistrict> areas) {
        this.preferredAreas.addAll(areas);
    }

    public Client(Long id) {
        this.id = id;
    }
}

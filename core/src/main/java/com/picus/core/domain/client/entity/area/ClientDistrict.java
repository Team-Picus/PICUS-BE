package com.picus.core.domain.client.entity.area;

import com.picus.core.domain.client.entity.Client;
import com.picus.core.domain.shared.area.entity.District;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(ClientDistrictId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientDistrict extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "client_no", nullable = false)
    private Client client;

    @Id
    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    public ClientDistrict(Client client, District district_id) {
        this.client = client;
        this.district = district;
    }
}

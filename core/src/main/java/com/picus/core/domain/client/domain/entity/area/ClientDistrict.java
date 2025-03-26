package com.picus.core.domain.client.domain.entity.area;

import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.shared.area.domain.entity.District;
import com.picus.core.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    public ClientDistrict(Client client, District district) {
        this.client = client;
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDistrict that = (ClientDistrict) o;
        return Objects.equals(client, that.client) && Objects.equals(district, that.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, district);
    }
}

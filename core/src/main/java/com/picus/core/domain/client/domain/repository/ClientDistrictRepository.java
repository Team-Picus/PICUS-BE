package com.picus.core.domain.client.domain.repository;

import com.picus.core.domain.client.domain.entity.area.ClientDistrict;
import com.picus.core.domain.client.domain.entity.area.ClientDistrictId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ClientDistrictRepository extends JpaRepository<ClientDistrict, ClientDistrictId> {
}

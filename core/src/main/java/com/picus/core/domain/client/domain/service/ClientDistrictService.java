package com.picus.core.domain.client.domain.service;

import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.entity.area.ClientDistrict;
import com.picus.core.domain.client.domain.repository.ClientDistrictRepository;
import com.picus.core.domain.shared.area.domain.entity.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientDistrictService {

    private final ClientDistrictRepository clientDistrictRepository;

    public Set<ClientDistrict> saveAll(Client client, Set<District> districts) {
        List<ClientDistrict> clientDistricts = districts.stream()
                .map(district -> new ClientDistrict(client, district))
                .toList();

        return new HashSet<>(clientDistrictRepository.saveAll(clientDistricts));
    }
}

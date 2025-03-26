package com.picus.core.domain.client.domain.service;

import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.entity.area.ClientDistrict;
import com.picus.core.domain.client.domain.repository.ClientDistrictRepository;
import com.picus.core.domain.shared.area.domain.entity.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientDistrictService {

    private final ClientDistrictRepository clientDistrictRepository;

    public List<ClientDistrict> saveAll(Client client, List<District> districts) {
        List<ClientDistrict> clientDistricts = districts.stream()
                .map(district -> new ClientDistrict(client, district))
                .toList();

        return clientDistrictRepository.saveAll(clientDistricts);
    }
}

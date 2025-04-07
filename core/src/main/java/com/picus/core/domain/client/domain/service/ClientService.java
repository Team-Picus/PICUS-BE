package com.picus.core.domain.client.domain.service;

import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.repository.ClientRepository;
import com.picus.core.domain.shared.area.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client save(Long userNo, Set<District> preferredAreas) {
        return clientRepository.save(new Client(userNo, preferredAreas));
    }
}

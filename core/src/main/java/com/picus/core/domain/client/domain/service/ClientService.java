package com.picus.core.domain.client.domain.service;

import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client save(Long userNo) {
        return clientRepository.save(new Client(userNo));
    }
}

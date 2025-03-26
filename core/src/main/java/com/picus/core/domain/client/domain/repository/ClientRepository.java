package com.picus.core.domain.client.domain.repository;

import com.picus.core.domain.client.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

package com.picus.core.old.domain.client.domain.repository;

import com.picus.core.old.domain.client.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

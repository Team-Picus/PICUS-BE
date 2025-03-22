package com.picus.core.domain.expert.domain.repository;

import com.picus.core.domain.expert.domain.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
}

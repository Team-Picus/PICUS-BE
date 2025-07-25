package com.picus.core.user.adapter.out.persistence.repository;

import com.picus.core.user.adapter.out.persistence.entity.UserTermEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserTermId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermJpaRepository extends JpaRepository<UserTermEntity, UserTermId> {
}

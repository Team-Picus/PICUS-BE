package com.picus.core.user.adapter.out.persistence.repository;

import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImageEntity, String> {

    @Query("select p from ProfileImageEntity p join UserEntity u where p.userNo = u.userNo and u.expertNo = :expertNo")
    Optional<ProfileImageEntity> findByExpertNo(String expertNo);
}

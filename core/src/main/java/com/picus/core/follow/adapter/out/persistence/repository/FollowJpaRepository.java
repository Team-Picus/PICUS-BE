package com.picus.core.follow.adapter.out.persistence.repository;

import com.picus.core.follow.adapter.out.persistence.entity.FollowEntity;
import com.picus.core.follow.adapter.out.persistence.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowJpaRepository extends JpaRepository<FollowEntity, FollowId> {

    @Query("select f from FollowEntity f where f.userNo = :user_no")
    List<FollowEntity> findByUserNo(@Param("user_no") String userNo);
}

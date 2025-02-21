package com.picus.core.domain.post.domain.repository;

import com.picus.core.domain.post.domain.entity.view.BackupViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupViewCountRepository extends JpaRepository<BackupViewCount, Long> {
}

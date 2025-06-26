package com.picus.core.old.global.oauth.repository;

import com.picus.core.old.global.oauth.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistBackupRepository extends JpaRepository<Blacklist, String> {
}

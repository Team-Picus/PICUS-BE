package com.picus.core.global.oauth.repository;

import com.picus.core.global.oauth.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistBackupRepository extends JpaRepository<Blacklist, String> {
}

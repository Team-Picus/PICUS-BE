package com.picus.core.domain.user.domain.service;

import com.picus.core.global.oauth.entity.Blacklist;
import com.picus.core.global.oauth.repository.BlacklistBackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistBackupService {

    private final BlacklistBackupRepository blacklistBackupRepository;

    public void save(Blacklist blacklist) {
        blacklistBackupRepository.save(blacklist);
    }
}

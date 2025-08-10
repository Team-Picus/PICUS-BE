package com.picus.core.reservation.adapter.out.persistence.repository;

import com.picus.core.reservation.application.port.out.ReservationBlacklistCreatePort;
import com.picus.core.reservation.application.port.out.ReservationBlacklistReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ReservationBlacklistRepository
        implements ReservationBlacklistCreatePort, ReservationBlacklistReadPort {

    private final RedisTemplate<String, String> redisTemplate;
    private final String prefix = "RESERVATION_BLACKLIST:";

    @Override
    public void create(String userNo) {
        redisTemplate.opsForValue().set(prefix + userNo, "", Duration.ofDays(1));
    }

    @Override
    public boolean isBlacklist(String userNo) {
        return redisTemplate.hasKey(prefix + userNo);
    }
}

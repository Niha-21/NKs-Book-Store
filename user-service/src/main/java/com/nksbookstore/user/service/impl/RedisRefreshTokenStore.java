package com.nksbookstore.user.service.impl;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nksbookstore.user.service.RefreshTokenStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenStore implements RefreshTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "refresh:";

    @Override
    public void save(String refreshToken, Long userId, Duration ttl) {
        redisTemplate.opsForValue()
                .set(PREFIX + refreshToken,
                     userId.toString(),
                     ttl);
    }

    @Override
    public Optional<Long> getUserId(String refreshToken) {
        String value = redisTemplate.opsForValue()
                .get(PREFIX + refreshToken);

        return value == null
                ? Optional.empty()
                : Optional.of(Long.parseLong(value));
    }

    @Override
    public void delete(String refreshToken) {
        redisTemplate.delete(PREFIX + refreshToken);
    }
}

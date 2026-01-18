package com.nksbookstore.user.service;

import java.time.Duration;
import java.util.Optional;

public interface RefreshTokenStore {

    void save(String refreshToken, Long userId, Duration ttl);

    Optional<Long> getUserId(String refreshToken);

    void delete(String refreshToken);
}

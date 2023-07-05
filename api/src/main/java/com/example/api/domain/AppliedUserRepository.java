package com.example.api.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AppliedUserRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Long add(Long userId) {
        //만약 set에 없다면 추가하고 1반환, 이미 있었으면 0반환
        return redisTemplate
                .opsForSet()
                .add("applied_User", userId.toString());
    }
}
;
package com.capstone.wanf.redis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {      // Redis 사용에 용이하도록 메서드화
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void setValues(String key, String value) {   // {key, value] 값을 저장
        redisTemplate.opsForValue().set(key, value);
    }

    // 값을 유효시간(timeout)과 함께 저장 (만료시간 설정 -> 자동 삭제)
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public String getValues(String key) {   // key 값을 사용해 value 값을 가져옴
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void deleteValues(String key) {  // key 값을 사용해 데이터를 삭제
        redisTemplate.delete(key);
    }
}

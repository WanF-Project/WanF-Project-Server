package com.capstone.wanf.auth.redis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValuesWithTimeout(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public void deleteValues(String key) {  // key 값을 사용해 데이터를 삭제
        redisTemplate.delete(key);
    }


    public String getValues(String key) {   // key 값을 사용해 value 값을 가져옴
        return redisTemplate.opsForValue().get(key);
    }
}

package com.capstone.wanf.auth.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
    private final RedisProperties redisProperties;

    // lettuce - 비동기, 넷티 기반의 Redis 클라이언트 라이브러리
    // 비동기 처리로 Jedis에 비해 몇배 이상의 성능과 하드웨어 자원 절약이 가능
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 클라이언트와 Redis 서버 간의 연결을 관리
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // redis-cli 사용을 위한 설정 (RedisTemplate)
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();    // Redis 서버와 상호 작용하기 위한 RedisTemplate 객체

        redisTemplate.setConnectionFactory(redisConnectionFactory());   // RedisConnectionFactory를 생성하고 RedisTemplate에 할당

        // Key, Value 값을 문자열(String)로 직렬화하는데 사용할 Serializer를 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}

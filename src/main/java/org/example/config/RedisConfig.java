package org.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<?, ?> redisCacheTemplate(
        @Qualifier("lettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory,
        @Qualifier("stringRedisSerializer") StringRedisSerializer stringRedisSerializer) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonSerializer());
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

    @Bean(name = "lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
    }

    @Bean(name = "jsonSerializer")
    public GenericJackson2JsonRedisSerializer jsonSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }
}

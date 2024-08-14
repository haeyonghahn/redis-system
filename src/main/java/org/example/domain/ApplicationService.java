package org.example.domain;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ApplicationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<Resolution> getData(String resolution) throws Exception {
        try {
            ResolutionCache resolutionCache = new ResolutionCache(redisTemplate);
            return resolutionCache.perFetch(resolution,1.0);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

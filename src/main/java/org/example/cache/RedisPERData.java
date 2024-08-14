package org.example.cache;

import java.io.Serializable;

/**
 * Cache redis에 담길 데이터구조 입니다.
 * @param cachedData 캐시의 데이터입니다.
 * @param expiryGapMs 실제 DB 조회 시간입니다.
 */
public class RedisPERData<T> implements Serializable {
	
	private T cachedData;
    private int expiryGapMs;

    public RedisPERData() {}

    public RedisPERData(T cachedData, int expiryGapMs) {
        this.cachedData = cachedData;
        this.expiryGapMs = expiryGapMs;
    }
	
    public T getCachedData() {
        return cachedData;
    }

    public void setCachedData(T cachedData) {
        this.cachedData = cachedData;
    }

    public int getExpiryGapMs() {
        return expiryGapMs;
    }

    public void setExpiryGapMs(int expiryGapMs) {
        this.expiryGapMs = expiryGapMs;
    }
}

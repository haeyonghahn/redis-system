package org.example.cache;

/**
 * Cache 정보를 담은 클래스입니다.
 * @param key 캐시의 key 값입니다.
 * @param cacheKeepSecond 캐시 만료 시간입니다.
 */
public class RedisCacheInfo {
	
	private String key;
    private int cacheKeepSecond;

    public RedisCacheInfo() {}

    public RedisCacheInfo(String key, int cacheKeepSecond) {
        this.key = key;
        this.cacheKeepSecond = cacheKeepSecond;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCacheKeepSecond() {
        return cacheKeepSecond;
    }

    public void setCacheKeepSecond(int cacheKeepSecond) {
        this.cacheKeepSecond = cacheKeepSecond;
    }
}

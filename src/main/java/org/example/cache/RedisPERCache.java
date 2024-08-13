package org.example.cache;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.utils.RedisUtil;

public abstract class RedisPERCache<T> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public T perFetch(String key, double beta) throws Exception {
		try {
			RedisCacheInfo cacheInfo = getCacheInfo(key);
			RedisPERData<T> redisData = findRedisCache(cacheInfo.getKey(), getTypeReference());
			if (redisData == null) {
				System.err.println("캐시 시간 만료 Cache Miss");
				return saveRedisPerCache(key);
			}

			/*
			 * pttl key
			 * 특정 key의 남은 생존 시간을 밀리초 단위로 반환하는 명령어입니다.
			 * 양수 : 키가 만료되기까지 남은 시간(밀리초).
			 * -1 : 키가 만료되지 않음(유효 기간이 설정되지 않음).
			 * -2 : 키가 존재하지 않음.
			 * ex) SETEX FN123456677 60 [{"companyCode":"5000","profitCenterCode":"1000","resolutionCode":"FN123456677"}]
			 *     PTTL FN123456677 -> 60000밀리초
			 */
			long expiryTtl = RedisUtil.pttl(cacheInfo.getKey());

			/*
			 * computationTime * beta * -log(random()) >= TTL
			 * 실제 DB 조회 시간(computationTime)에 확률적인 랜덤 값(-log(random())을 곱하여 캐시 만료 시간(TTL)과 비교해 재캐싱 여부 결정
			 * 재캐싱 여부 범위를 늘려주기 위해 beta 상수를 높여서 설정해준다. (default : 1.0)
			 */
			double gapScore = redisData.getExpiryGapMs() * beta * -Math.log(new Random().nextDouble());

			if (gapScore >= expiryTtl) {
				System.err.println("PER 알고리즘 Cache Miss");
				return saveRedisPerCache(key);
			}
			return redisData.getCachedData();
		} catch (Exception e) {
			throw new Exception(e);
		}
    }
	
	private RedisPERData<T> findRedisCache(String key, TypeReference<RedisPERData<T>> typeReference) throws Exception {
		try {
			String value = RedisUtil.get(key);
			if (value != null) {
				byte[] data = value.getBytes();
				return deserialize(data, typeReference);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	private T saveRedisPerCache(String key) throws Exception {
		try {
			// ========= DB 조회 시간 ============== //
			long startTime = System.nanoTime();
			T resultFromDb = getResourceData();
			long processedTime = System.nanoTime() - startTime;
			int expiryGapMs = (int) TimeUnit.NANOSECONDS.toMillis(processedTime);
			// ========= DB 조회 시간 ============== //

			RedisCacheInfo cacheInfo = getCacheInfo(key);
			RedisPERData<T> data = new RedisPERData<>(resultFromDb, expiryGapMs);
			/*
			 * SETEX key seconds value
			 * 특정 키에 대해 값을 설정하고, 해당 키의 만료 시간을 초 단위로 지정하는 명령어입니다.
			 * key : 값을 저장할 키 이름입니다.
			 * seconds : 키가 만료될 때까지의 시간(초 단위)입니다.
			 * value : 저장할 값입니다.
			 * ex) 특정 키에 대해 60초 동안 유효한 값을 설정하시오.
			 * SETEX FN123456677 60 [{"companyCode":"5000","profitCenterCode":"1000","resolutionCode":"FN123456677"}]
			 */
			RedisUtil.setex(cacheInfo.getKey(), cacheInfo.getCacheKeepSecond(), serialize(data));

			return resultFromDb;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private <V> V deserialize(byte[] data, TypeReference<V> typeReference) {
		try {
			return objectMapper.readValue(data, typeReference);
		} catch (IOException e) {
            throw new RuntimeException("Deserialization error", e);
        }
	}
	
	private byte[] serialize(RedisPERData<T> data) {
		try {
            return objectMapper.writeValueAsBytes(data);
        } catch (IOException e) {
            throw new RuntimeException("Serialization error", e);
        }
    }

	protected abstract TypeReference<RedisPERData<T>> getTypeReference() throws Exception;
	protected abstract RedisCacheInfo getCacheInfo(String key) throws Exception;
	protected abstract T getResourceData() throws Exception;
}

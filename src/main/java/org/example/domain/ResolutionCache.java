package org.example.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.cache.RedisCacheInfo;
import org.example.cache.RedisPERCache;
import org.example.cache.RedisPERData;

import java.util.ArrayList;
import java.util.List;

public class ResolutionCache extends RedisPERCache<List<Resolution>> {

	private static final int RESOLUTION_LIST_CACHE_TTL = 60;

	@Override
	protected TypeReference<RedisPERData<List<Resolution>>> getTypeReference() {
		return new TypeReference<RedisPERData<List<Resolution>>>() {};
	}

	@Override
	protected RedisCacheInfo getCacheInfo(String key) {
		return new RedisCacheInfo(key, RESOLUTION_LIST_CACHE_TTL);
	}

	@Override
	protected List<Resolution> getResourceData() {
		List<Resolution> resolutions = new ArrayList<>();
		Resolution resolution = new Resolution();
		resolution.setCompanyCode("5000");
		resolution.setProfitCenterCode("1000");
		resolution.setResolutionCode("FN123456677");
		resolutions.add(resolution);
		return resolutions;
	}
}

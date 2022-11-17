package com.example.cache;

import java.util.Collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class CacheUtils {
	
	private static final Map<String, String> CACHELIST = new HashMap<>(); // 캐쉬리스트
	private static final Map<String, Integer> CACHEHIT = new HashMap<>(); // 캐쉬HIT
	
	// 캐시 사이즈, 5개 제한
	private static final int MAXSIZE = 5;
	
	// 캐시 조회
	public static String getCache(String key) {
		if(!CACHELIST.containsKey(key)) {
			return null;
		}
		
		CACHEHIT.put(key, CACHEHIT.get(key) + 1);
		return CACHELIST.get(key);
	}
	
	// 캐시 저장
	public static void saveCache(String key, String value) {
		if(isMaxSize()) {
			String minKey = getMinValue().getKey();
			CACHELIST.remove(minKey);
			CACHEHIT.remove(minKey);
		}
		CACHELIST.put(key, value);
		saveCacheHit(key);
	}

	// 캐시 히트 저장
	private static void saveCacheHit(String key) {
		CACHEHIT.put(key, 0);
	}
	
	// 캐시 한도 체크
	// true: 한도 초과
	private static boolean isMaxSize() {
		return CACHELIST.size() >= MAXSIZE ? true : false;
	}
	
	// 최소 CACHEHIT Map 가져오기
	private static Entry<String, Integer> getMinValue() {
		return Collections.min(CACHEHIT.entrySet(), (v1, v2) -> v1.getValue().compareTo(v2.getValue()));
	}
	
}

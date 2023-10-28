package com.bbsk.cache.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class CacheUtil {
	
	private static final Map<String, String> CACHELIST = new HashMap<>(); // 캐쉬리스트
	private static final Map<String, Integer> CACHEHIT = new HashMap<>(); // 캐쉬HIT
	private static final Map<String, LocalDateTime> CACHEDATE = new HashMap<>(); // 캐쉬 생성시간
	
	// 캐시 사이즈, 5개 제한
	private static final int MAXSIZE = 5;
	// 캐시 만료시간 5분, 300초
	private static final int EXPIREDMINUTES = 300;
	
	// 캐시 조회
	public static String getCache(String key) {
		LocalDateTime now = LocalDateTime.now();
		
		// 캐시가 있는지
		if(CACHELIST.containsKey(key)) {
			// 캐시 시간이 유효시간이 맞는지
			if(Duration.between(getExpiredCacheTime(key), now).getSeconds() >= EXPIREDMINUTES) {
				deleteCache(key);
				return null;
			}
		} else {
			return null;
		}
		
		CACHEHIT.put(key, CACHEHIT.get(key) + 1);
		CACHEDATE.put(key, now);
		return CACHELIST.get(key);
	}

	// 캐시 삭제
	private static void deleteCache(String key) {
		CACHELIST.remove(key);
		CACHEHIT.remove(key);
		CACHEDATE.remove(key);
	}
	
	// 캐시 저장
	public static void saveCache(String key, String value) {
		if(isMaxSize()) {
			String minKey = getMinValue().getKey();
			deleteCache(minKey);
		}
		CACHELIST.put(key, value);
		CACHEDATE.put(key, LocalDateTime.now());
		CACHEHIT.put(key, 0);
	}
	
	// 캐시 초기화
	public static void initCache() {
		CACHELIST.clear();
		CACHEHIT.clear();
		CACHEDATE.clear();
	}

	// 캐시 한도 체크
	// true: 한도 초과
	public static boolean isMaxSize() {
		return CACHELIST.size() >= MAXSIZE ? true : false;
	}
	
	// 최소 CACHEHIT Map 가져오기
	private static Entry<String, Integer> getMinValue() {
		return Collections.min(CACHEHIT.entrySet(), (v1, v2) -> v1.getValue().compareTo(v2.getValue()));
	}
	
	// key에 해당하는 캐시 생성 시간 가져오기
	public static LocalDateTime getCacheTime(String key) {
		return CACHEDATE.get(key);
	}
  
	// key에 해당하는 캐시 만료시간 가져오기, 생성시간+5분
	public static LocalDateTime getExpiredCacheTime(String key) {
		return getCacheTime(key).plusMinutes(Duration.ofMinutes(5L).toMinutes());
	}
}
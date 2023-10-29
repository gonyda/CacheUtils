package com.bbsk.cache.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bbsk.cache.domain.Cache;
import com.bbsk.cache.domain.CacheBuilder;

public final class CacheUtil {
	
	private static final Map<String, Cache> CACHELIST = new HashMap<>(); // 캐쉬 리스트
	
	// 캐시 사이즈, 5개 제한
	private static final int MAXSIZE = 5;
	// 캐시 만료시간 5분, 300초
	private static final int EXPIREDMINUTES = 300;
	
	// 캐시 조회
	public static Cache getCache(String key) {
		LocalDateTime now = LocalDateTime.now();
		
		return isExistCache(key, now) ? CACHELIST.get(updateCache(key, now)) : null;
	}
	
	// 캐시 초기 저장
	public static void saveCache(String key, String value) {
		if(isMaxSize()) {
			CACHELIST.remove(getMinHitKey());
		}
		
		CACHELIST.put(key, createCache(value, 0, LocalDateTime.now()));
	}
	
	// 캐시 업데이트, 히트 수 +1 / 생성시간 최신화
	private static String updateCache(String key, LocalDateTime now) {
		Cache cache = CACHELIST.get(key);
		
		CACHELIST.put(key, createCache(cache.getValue(), cache.getHit() + 1, now));
		
		return key;
	}
	
	// 캐시리스트 초기화
	public static void initCache() {
		CACHELIST.clear();
	}

	// 캐시 한도 체크
	// true: 한도 초과
	public static boolean isMaxSize() {
		return CACHELIST.size() >= MAXSIZE ? true : false;
	}
	
	// 모든 캐시 가져오기
	public static List<Cache> getCacheAll() {
		List<Cache> list = new ArrayList<>();
		
		CACHELIST.values().forEach(e -> list.add(e));
		
		return list;
	}
	
	// 캐쉬 객체 생성
	private static Cache createCache(String value, int hit, LocalDateTime now) {
		return new CacheBuilder()
					.value(value)
					.hit(hit)
					.createDateTime(now)
					.build();
	}
	
	// 유효한 key값인지 체크
	private static boolean isExistCache(String key, LocalDateTime now) {
		// 캐시가 있는지
		if(!CACHELIST.containsKey(key)) {
			return false;
		}
		
		// 캐시 시간이 유효시간이 맞는지
		if(getDiffTimeByExpiredAndNow(key, now) >= EXPIREDMINUTES) {
			CACHELIST.remove(key);
			return false;
		}
		
		return true;
	}
	
	// 캐시 만료시간과 현재시간 초 차이 가져오기
	private static long getDiffTimeByExpiredAndNow(String key, LocalDateTime now) {
		return Duration.between(CACHELIST.get(key).getExpiredDateTime(), now).getSeconds();
	}

	// 최소 hit을 가진 cache의 key값 구하기
	private static String getMinHitKey() {
		String minHitKey = null;
		int minHit = Integer.MAX_VALUE;

		for (Entry<String, Cache> cache : CACHELIST.entrySet()) {
		    if (cache.getValue().getHit() < minHit) {
		        minHit = cache.getValue().getHit();
		        minHitKey = cache.getKey();
		    }
		}
		
		return minHitKey;
	}
}
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

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class CacheUtil {
	
	private static final Map<Object, Cache> CACHELIST = new HashMap<>(); // 캐쉬 리스트
	
	// 캐시 사이즈, 5개 제한
	private static final int MAXSIZE = 5;
	// 캐시 만료시간 5분, 300초
	private static final int EXPIREDMINUTES = 300;

	// 캐시 조회
	public static <K> Cache getCache(K key) {
		return isExistCache(key) ? CACHELIST.get(updateCache(key)) : null;
	}
	
	// 캐시 초기 저장
	public static <K, V> void saveCache(K key, V value) {
		if(isMaxSize()) {
			CACHELIST.remove(getMinHitKey());
		}
		
		CACHELIST.put(key, getCacheObject(value, 0));
	}
	
	// 모든 캐시 가져오기
	public static List<Cache> getCacheAll() {
		List<Cache> list = new ArrayList<>();
		
		CACHELIST.values().forEach(e -> list.add(e));
		
		return list;
	}
	
	// 캐시리스트 초기화
	public static void initCache() {
		CACHELIST.clear();
	}
	
	// 캐시 업데이트, 히트 수 +1 / 생성시간 최신화
	private static <K> K updateCache(K key) {
		Cache cache = CACHELIST.get(key);
		
		CACHELIST.put(key, getCacheObject(cache.getValue(), cache.getHit() + 1));
		
		return key;
	}

	// 캐시 한도 체크
	// true: 한도 초과
	private static boolean isMaxSize() {
		return CACHELIST.size() >= MAXSIZE ? true : false;
	}
	
	// 캐쉬 객체 생성
	
	private static <V> Cache getCacheObject(V value, int hit) {
		return new CacheBuilder()
					.value(value)
					.hit(hit)
					.build();
	}
	
	// 유효한 key값인지 체크
	private static <K> boolean isExistCache(K key) {
		// 캐시가 있는지
		if(!CACHELIST.containsKey(key)) {
			return false;
		}
		
		// 캐시 시간이 유효시간이 맞는지
		if(getDiffTimeByExpiredAndNow(key, LocalDateTime.now()) >= EXPIREDMINUTES) {
			CACHELIST.remove(key);
			return false;
		}
		
		return true;
	}
	
	// 캐시 만료시간과 현재시간 초 차이 가져오기
	private static <K> long getDiffTimeByExpiredAndNow(K key, LocalDateTime now) {
		return Duration.between(now, CACHELIST.get(key).getExpiredDateTime()).getSeconds();
	}

	// 최소 hit을 가진 cache의 key값 구하기
	private static <K> K getMinHitKey() {
		K minHitKey = null;
		int minHit = Integer.MAX_VALUE;

		for (Entry<Object, Cache> cache : CACHELIST.entrySet()) {
		    if (cache.getValue().getHit() < minHit) {
		        minHit = cache.getValue().getHit();
		        minHitKey = (K) cache.getKey();
		    }
		}
		
		return minHitKey;
	}
}
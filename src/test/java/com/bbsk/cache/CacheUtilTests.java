package com.bbsk.cache;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bbsk.cache.constant.JobCode;
import com.bbsk.cache.utils.CacheUtil;

class CacheUtilTests {
	
	@BeforeEach
	void before() {
		CacheUtil.initCache();
	}
	
	@DisplayName("캐시 초기화")
	@Test
	void initCache() {
		CacheUtil.saveCache(JobCode.MVS, JobCode.MVS.getValue());
		
		CacheUtil.initCache();
		
		assertThat(CacheUtil.getCache(JobCode.MVS)).isNull();
	}
	
	@DisplayName("캐시가 없다면 null 반환")
	@Test
	void nullCheck() {
		assertThat(CacheUtil.getCache(JobCode.MVS.getValue())).isNull();
	}
	
	@DisplayName("캐시가 있다면 캐시 반환")
	@Test
	void existCache() {
		
		CacheUtil.saveCache(JobCode.MVS, JobCode.MVS.getValue());
		
		assertThat(CacheUtil.getCache(JobCode.MVS).getValue()).isEqualTo(JobCode.MVS.getValue());
	}
	
	@DisplayName("캐시 리스트 저장 테스트")
	@Test
	void save() {
		CacheUtil.saveCache(JobCode.TCT, JobCode.TCT.getValue());
		CacheUtil.saveCache(JobCode.TCB, JobCode.TCB.getValue());
		CacheUtil.saveCache(JobCode.SMT, JobCode.SMT.getValue());
		
		assertThat(CacheUtil.getCache(JobCode.TCT)).isNotNull();
		assertThat(CacheUtil.getCache(JobCode.TCB)).isNotNull();
		assertThat(CacheUtil.getCache(JobCode.SMT)).isNotNull();
	}
	
	@DisplayName("캐시조회 hit 적중률 검사")
	@Test
	void cacheHit() {
		saveCacheAfterRepeatGetCache(JobCode.TCT, JobCode.TCT.getValue(), 3);
		saveCacheAfterRepeatGetCache(JobCode.TCB, JobCode.TCB.getValue(), 2);
		saveCacheAfterRepeatGetCache(JobCode.SMT, JobCode.SMT.getValue(), 6);
		
		assertThat(CacheUtil.getCache(JobCode.TCT).getHit()).isEqualTo(4);
		assertThat(CacheUtil.getCache(JobCode.TCB).getHit()).isEqualTo(3);
		assertThat(CacheUtil.getCache(JobCode.SMT).getHit()).isEqualTo(7);
	}
	
	@DisplayName("캐시 사이즈 초과 시 최소 hit 캐시 삭제")
	@Test
	void cacheSize() {
		saveCacheAfterRepeatGetCache(JobCode.TCT, JobCode.TCT.getValue(), 1);
		saveCacheAfterRepeatGetCache(JobCode.TCB, JobCode.TCB.getValue(), 2);
		saveCacheAfterRepeatGetCache(JobCode.SMT, JobCode.SMT.getValue(), 3);
		saveCacheAfterRepeatGetCache(JobCode.MVS, JobCode.MVS.getValue(), 4);
		saveCacheAfterRepeatGetCache(JobCode.WIB, JobCode.WIB.getValue(), 5);
		
		saveCacheAfterRepeatGetCache(JobCode.WIT, JobCode.WIT.getValue(), 9);
		
		assertThat(CacheUtil.getCache(JobCode.TCT)).isNull();
	}
	
	@DisplayName("캐시 유효시간 초과 시 해당 캐시 삭제")
	@Test
	void expiredTime() {
		// 현재시간 -20분
		LocalDateTime now = LocalDateTime.now().minusMinutes(Duration.ofMinutes(20L).toMinutes());
		saveCacheAfterRepeatGetCache(JobCode.TCT, JobCode.TCT.getValue(), 1);
		
		LocalDateTime expiredDateTime = CacheUtil.getCache(JobCode.TCT).getExpiredDateTime();
		
		assertThat(Duration.between(now, expiredDateTime).getSeconds()).isGreaterThan(300L);
	}
	
	@DisplayName("모든 캐시 가져오기")
	@Test
	void getCacheAll() {
		saveCacheAfterRepeatGetCache(JobCode.TCT, JobCode.TCT.getValue(), 0);
		saveCacheAfterRepeatGetCache(JobCode.TCB, JobCode.TCB.getValue(), 1);
		saveCacheAfterRepeatGetCache(JobCode.SMT, JobCode.SMT.getValue(), 2);
		saveCacheAfterRepeatGetCache(JobCode.MVS, JobCode.MVS.getValue(), 3);
		
		assertThat(CacheUtil.getCacheAll()).hasSize(4);
	}
	
	@DisplayName("캐시 저장 제네릭타입 테스트")
	@Test
	void genericType() {
		CacheUtil.saveCache(JobCode.MVS, JobCode.MVS.getValue());
		CacheUtil.saveCache("TCT", 2);
		CacheUtil.saveCache(4, "2");
		CacheUtil.saveCache(JobCode.TCT, JobCode.TCT);
	
		assertThat(CacheUtil.getCache(JobCode.MVS).getValue()).isEqualTo(JobCode.MVS.getValue());
		assertThat(CacheUtil.getCache("TCT").getValue()).isEqualTo(2);
		assertThat(CacheUtil.getCache(4).getValue()).isEqualTo("2");
		assertThat(CacheUtil.getCache(JobCode.TCT).getValue()).isEqualTo(JobCode.TCT);
	}

	// 캐시 저장 후 캐시조회 반복
	private void saveCacheAfterRepeatGetCache(JobCode key, String value, int count) {
		// saveCache()를 하기 때문에
		// cache hit = count + 1
		CacheUtil.saveCache(key, value);
		for (int i = 0; i < count; i++) {
			CacheUtil.getCache(key);
		}
	}
	
}

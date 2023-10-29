package com.bbsk.cache;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bbsk.cache.constant.JobCode;
import com.bbsk.cache.utils.CacheUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CacheUtilTests {
	
	@DisplayName("캐시 만료시간")
	@Test
	void expiredTime() {
		CacheUtil.initCache();
		
		// given
		String key = JobCode.MVS.toString();
		String value = JobCode.MVS.getJobCode();
		
		// when
		CacheUtil.saveCache(key, value);
		LocalDateTime minusCurrentTime = LocalDateTime.now().minusMinutes(Duration.ofMinutes(10L).toMinutes());
		LocalDateTime plusCurrentTime = LocalDateTime.now().plusMinutes(Duration.ofMinutes(10L).toMinutes());
		
		// then
		LocalDateTime expiredCacheTime = CacheUtil.getCache(key).getExpiredDateTime();
		// 만료시간이 현재시간보다 더 크다면
		assertThat(expiredCacheTime).isAfter(minusCurrentTime);
		// 현재시간이 만료시간보다 더 크다면
		assertThat(plusCurrentTime).isAfter(expiredCacheTime);
		
		log.debug("expiredTime: {}", expiredCacheTime);
		log.debug("마이너스 현재시간: {}", minusCurrentTime);
		log.debug("플러스 현재시간: {}", plusCurrentTime);
	}
	
	@DisplayName("캐시 저장")
	@Test
	void saveCache() {
		CacheUtil.initCache();
		
		// given : 테스트시 미리 주어져야할 조건들은 무엇일까?
		String key = JobCode.MVS.toString();
		String value = JobCode.MVS.getJobCode();
		
		// when : 테스트 실행
		CacheUtil.saveCache(key, value);
		
		// then : 테스트 검증
		String res = CacheUtil.getCache(key).getValue();
		assertThat(res).isEqualTo(value);
	}
	
	@DisplayName("캐시 저장 한도")
	@Test
	void maxSaveCache() {
		CacheUtil.initCache(); 
		
		// given
		CacheUtil.saveCache(JobCode.TCT.toString(), JobCode.TCT.getJobCode());
		CacheUtil.saveCache(JobCode.TCB.toString(), JobCode.TCB.getJobCode());
		CacheUtil.saveCache(JobCode.WIT.toString(), JobCode.WIT.getJobCode());
		CacheUtil.saveCache(JobCode.WIB.toString(), JobCode.WIB.getJobCode());
		
		// when
		CacheUtil.saveCache(JobCode.MVS.toString(), JobCode.MVS.getJobCode());
		boolean isMaxSize = CacheUtil.isMaxSize();
		
		// then
		assertThat(isMaxSize).isEqualTo(true);
	}
	
	@DisplayName("캐시 적중률 테스트")
	@Test
	void priorityCache() {
		CacheUtil.initCache();
		
		// 1...5 -> 1번째를 미친듯이 조회. 그후 6번째 넣었을때
		// 2->5, 3->4 5->1, 1->3, 4->5
		// 6, 7, 8, 9
		
		// given
		String mvs = JobCode.MVS.toString();
		String mvsjobCode = JobCode.MVS.getJobCode();
		
		String smt = JobCode.SMT.toString();
		String smtjobCode = JobCode.SMT.getJobCode();
		
		String wit = JobCode.WIT.toString();
		String witjobCode = JobCode.WIT.getJobCode();
		
		String wib = JobCode.WIB.toString();
		String wibjobCode = JobCode.WIB.getJobCode();
		
		String tct = JobCode.TCT.toString();
		String tctjobCode = JobCode.TCT.getJobCode();
		
		String tcb = JobCode.TCB.toString();
		String tcbjobCode = JobCode.TCB.getJobCode();
		
		String trm = JobCode.TRM.toString();
		String trmjobCode = JobCode.TRM.getJobCode();
		
		String ttm = JobCode.TTM.toString();
		String ttmjobCode = JobCode.TTM.getJobCode();
		
		// when
		// mvs hit: 8
		if(CacheUtil.getCache(mvs) == null) {
			CacheUtil.saveCache(mvs, mvsjobCode);
			for (int i = 0; i < 7; i++) {
				CacheUtil.getCache(mvs);
			}
		}
		
		// smt hit: 3
		if(CacheUtil.getCache(smt) == null) {
			CacheUtil.saveCache(smt, smtjobCode);
			for (int i = 0; i < 3; i++) {
				CacheUtil.getCache(smt);
			}
		}
		
		// wit hit: 6
		if(CacheUtil.getCache(wit) == null) {
			CacheUtil.saveCache(wit, witjobCode);
			for (int i = 0; i < 6; i++) {
				CacheUtil.getCache(wit);
			}
		}
		
		// wib hit: 5
		if(CacheUtil.getCache(wib) == null) {
			CacheUtil.saveCache(wib, wibjobCode);
			for (int i = 0; i < 5; i++) {
				CacheUtil.getCache(wib);
			}
		}
		
		// tct hit: 9
		if(CacheUtil.getCache(tct) == null) {
			CacheUtil.saveCache(tct, tctjobCode);
			for (int i = 0; i < 9; i++) {
				CacheUtil.getCache(tct);
			}
		}
				
				
		// HIT 수
		// mvs : 8
		// smt : 4
		// wit : 7
		// wib : 6
		// tct : 10
		
		// then
		
		// tcb hit: 10
		if(CacheUtil.getCache(tcb) == null) {
			CacheUtil.saveCache(tcb, tcbjobCode);
			for (int i = 0; i < 9; i++) {
				CacheUtil.getCache(tcb);
			}
		}
		assertThat(CacheUtil.getCache(smt)).isNull();
		
		// trm hit: 2
		if(CacheUtil.getCache(trm) == null) {
			CacheUtil.saveCache(trm, trmjobCode);
			for (int i = 0; i < 1; i++) {
				CacheUtil.getCache(trm);
			}
		}
		
		assertThat(CacheUtil.getCache(wib)).isNull();
		
		// tct hit: 12
		for (int i = 0; i < 3; i++) {
			CacheUtil.getCache(tct);
		}
		
		
		// ttm 3
		if(CacheUtil.getCache(ttm) == null) {
			CacheUtil.saveCache(ttm, ttmjobCode);
			for (int i = 0; i < 3; i++) {
				CacheUtil.getCache(ttm);
			}
		}
		
		CacheUtil.getCacheAll().forEach(e -> log.debug(e.toString()));
		
		assertThat(CacheUtil.getCache(trm)).isNull();
		assertThat(CacheUtil.getCache(ttm).getValue()).isEqualTo(ttmjobCode);
		
	}
	
}

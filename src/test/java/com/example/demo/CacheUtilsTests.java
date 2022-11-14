package com.example.demo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.cache.CacheUtils;
import com.example.cache.JobCode;

class CacheUtilsTests {

	@DisplayName("캐시 저장")
	@Test
	void saveCache() {
//		// given : 테스트시 미리 주어져야할 조건들은 무엇일까?
//		String key = JobCode.MVS.toString();
//		String value = JobCode.MVS.getJobCode();
//		
//		// when : 테스트 실행
//		CacheUtils.saveCache(key, value);
//		
//		// then : 테스트 검증
//		String res = CacheUtils.getCache(key);
//		assertThat(res).isEqualTo(value);
	}
	
	@DisplayName("캐시 저장 한도")
	@Test
	void maxSaveCache() {
//		// given
//		CacheUtils.saveCache(JobCode.TCT.toString(), JobCode.TCT.getJobCode());
//		CacheUtils.saveCache(JobCode.TCB.toString(), JobCode.TCB.getJobCode());
//		CacheUtils.saveCache(JobCode.WIT.toString(), JobCode.WIT.getJobCode());
//		CacheUtils.saveCache(JobCode.WIB.toString(), JobCode.WIB.getJobCode());
//		
//		// when
//		CacheUtils.saveCache(JobCode.MVS.toString(), JobCode.MVS.getJobCode());
//		boolean isMaxSize = CacheUtils.isMaxSize();
//		
//		// then
//		assertThat(isMaxSize).isEqualTo(true);
	}
	
	@DisplayName("캐시 적중률 테스트")
	@Test
	void priorityCache() {
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
		if(CacheUtils.getCache(mvs) == null) {
			CacheUtils.saveCache(mvs, mvsjobCode);
			for (int i = 0; i < 7; i++) {
				CacheUtils.getCache(mvs);
			}
		}
		
		// smt hit: 3
		if(CacheUtils.getCache(smt) == null) {
			CacheUtils.saveCache(smt, smtjobCode);
			for (int i = 0; i < 3; i++) {
				CacheUtils.getCache(smt);
			}
		}
		
		// wit hit: 6
		if(CacheUtils.getCache(wit) == null) {
			CacheUtils.saveCache(wit, witjobCode);
			for (int i = 0; i < 6; i++) {
				CacheUtils.getCache(wit);
			}
		}
		
		// wib hit: 5
		if(CacheUtils.getCache(wib) == null) {
			CacheUtils.saveCache(wib, wibjobCode);
			for (int i = 0; i < 5; i++) {
				CacheUtils.getCache(wib);
			}
		}
		
		// tct hit: 9
		if(CacheUtils.getCache(tct) == null) {
			CacheUtils.saveCache(tct, tctjobCode);
			for (int i = 0; i < 9; i++) {
				CacheUtils.getCache(tct);
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
		if(CacheUtils.getCache(tcb) == null) {
			CacheUtils.saveCache(tcb, tcbjobCode);
			for (int i = 0; i < 9; i++) {
				CacheUtils.getCache(tcb);
			}
		}
		assertThat(CacheUtils.getCache(smt)).isNull();
		
		// trm hit: 2
		if(CacheUtils.getCache(trm) == null) {
			CacheUtils.saveCache(trm, trmjobCode);
			for (int i = 0; i < 1; i++) {
				CacheUtils.getCache(trm);
			}
		}
		
		assertThat(CacheUtils.getCache(wib)).isNull();
		
		// tct hit: 13 
		for (int i = 0; i < 3; i++) {
			CacheUtils.getCache(tct);
		}
		
		
		// ttm 4
		if(CacheUtils.getCache(ttm) == null) {
			CacheUtils.saveCache(ttm, ttmjobCode);
			for (int i = 0; i < 3; i++) {
				CacheUtils.getCache(ttm);
			}
		}
		
		assertThat(CacheUtils.getCache(trm)).isNull();
		assertThat(CacheUtils.getCache(ttm)).isEqualTo(ttmjobCode);
		
	}
	
}

package com.bbsk.cache.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class CacheBuilder {

	private String value; // 캐쉬 값
	private int hit; // 캐쉬 히트 
	private LocalDateTime createDateTime; // 캐쉬 생성 시간
	private LocalDateTime expiredDateTime; // 캐쉬 만료 시간, 생성시간 + 5분
	
	public CacheBuilder value(String value) {
		this.value = value;
		return this;
	}
	
	public CacheBuilder hit(int hit) {
		this.hit = hit;
		return this;
	}
	
	public CacheBuilder createDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
		this.expiredDateTime = createDateTime.plusMinutes(Duration.ofMinutes(5L).toMinutes());
		return this;
	}
	
	public Cache build() {
		return new Cache(value, hit, createDateTime, expiredDateTime);
	}
}

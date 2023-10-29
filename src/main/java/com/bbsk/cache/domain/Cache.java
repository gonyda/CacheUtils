package com.bbsk.cache.domain;

import java.time.LocalDateTime;

import lombok.ToString;

@ToString
public class Cache {

	private String value; // 캐쉬 값
	private int hit; // 캐쉬 히트 
	private LocalDateTime createDateTime; // 캐쉬 생성 시간
	private LocalDateTime expiredDateTime; // 캐쉬 만료 시간, 생성시간 + 5분
	
	public Cache(String value, int hit, LocalDateTime createDateTime, LocalDateTime expiredDateTime) {
		super();
		this.value = value;
		this.hit = hit;
		this.createDateTime = createDateTime;
		this.expiredDateTime = expiredDateTime;
	}

	public String getValue() {
		return value;
	}
	
	public int getHit() {
		return hit;
	}
	
	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
	
	public LocalDateTime getExpiredDateTime() {
		return expiredDateTime;
	}
	
	
}

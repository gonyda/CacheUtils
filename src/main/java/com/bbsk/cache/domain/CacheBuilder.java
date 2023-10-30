package com.bbsk.cache.domain;

public class CacheBuilder {

	private String value; // 캐쉬 값
	private int hit; // 캐쉬 히트 
	
	public CacheBuilder value(String value) {
		this.value = value;
		return this;
	}
	
	public CacheBuilder hit(int hit) {
		this.hit = hit;
		return this;
	}
	
	public Cache build() {
		return new Cache(value, hit);
	}
}

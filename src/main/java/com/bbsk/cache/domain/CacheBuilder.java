package com.bbsk.cache.domain;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CacheBuilder <V> {

	private V value; // 캐쉬 값
	private int hit; // 캐쉬 히트 
	
	public CacheBuilder value(V value) {
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

package com.bbsk.cache.constant;

public enum JobCode {
	TCT("무선해지미납"),
	TCB("유선해지미납"),
	WIB("유선"),
	WIT("통합"),
	SMT("스마트폰"),
	MVS("모비우스"),
	TRM("해지후할부"),
	TTM("단말해지미납");

	private String jobCode;
	
	JobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
	public String getJobCode() {
		return jobCode;
		
	}
}

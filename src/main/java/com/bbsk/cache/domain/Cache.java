package com.bbsk.cache.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Cache {

	private int hit;
	private LocalDate createDateTime;
}

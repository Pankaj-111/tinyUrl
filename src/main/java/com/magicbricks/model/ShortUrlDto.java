package com.magicbricks.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ShortUrlDto {
	private Long id;
	private String shortUrl;
	private String fullUrl;
}

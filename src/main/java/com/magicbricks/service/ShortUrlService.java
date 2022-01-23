package com.magicbricks.service;

import com.magicbricks.model.ShortUrlDto;
import com.magicbricks.model.ShortUrlRequest;

import reactor.core.publisher.Mono;

public interface ShortUrlService {
	Mono<ShortUrlDto> getShortUrl(final ShortUrlRequest request);
}

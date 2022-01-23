package com.magicbricks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magicbricks.model.ShortUrlDto;
import com.magicbricks.model.ShortUrlRequest;
import com.magicbricks.service.ShortUrlService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/shorturl")
public class TinyUrlGeneratorApi {
	@Autowired
	private ShortUrlService service;

	@PostMapping("/create")
	public Mono<ShortUrlDto> createShortUrl(@RequestBody @Validated  ShortUrlRequest request) {
		return service.getShortUrl(request);		 
	}
}

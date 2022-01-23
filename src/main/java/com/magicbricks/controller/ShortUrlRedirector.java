package com.magicbricks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.magicbricks.service.RedirectorService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ShortUrlRedirector {

	@Autowired
	private RedirectorService service;

	@RequestMapping("/{type}/{code}")
	public Mono<String> getUrl(@PathVariable final String type, @PathVariable final String code, final Model model,
			final ServerHttpRequest req) {
		log.info("Url type :{}, code: {}", type, code);
		try {
			service.validateUrlType(type);
			final Mono<String> fullUrl = service.getUrlByShortCode(code);
			model.addAttribute("url", fullUrl);
		} catch (Exception ex) {
			log.error("Exception :", ex);
			return Mono.just("error");
		}
		return Mono.just("result");
	}
}

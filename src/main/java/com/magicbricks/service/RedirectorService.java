package com.magicbricks.service;

import com.magicbricks.exceptions.UrlTypeException;

import reactor.core.publisher.Mono;

public interface RedirectorService {
boolean validateUrlType(String urlTye) throws UrlTypeException;

Mono<String> getUrlByShortCode(String code);
}

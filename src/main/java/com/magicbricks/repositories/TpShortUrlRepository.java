package com.magicbricks.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.magicbricks.entity.TpshortUrl;

import reactor.core.publisher.Mono;

public interface TpShortUrlRepository extends ReactiveCrudRepository<TpshortUrl, Long> {

	@Query("select u.* from tpshort_url u where u.fullurl=:fullUrl order by u.modidate desc limit 1")
	public Mono<TpshortUrl> getByFullUrl(final String fullUrl);

	@Query("select u.* from tpshort_url u where u.shorturl=:shorturl limit 1")
	public Mono<TpshortUrl> getByShortUrl(final String shorturl);
}

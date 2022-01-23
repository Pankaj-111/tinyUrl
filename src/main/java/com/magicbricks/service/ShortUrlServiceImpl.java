package com.magicbricks.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicbricks.entity.TpshortUrl;
import com.magicbricks.exceptions.ShortCodeExistExceptio;
import com.magicbricks.model.ShortUrlDto;
import com.magicbricks.model.ShortUrlRequest;
import com.magicbricks.repositories.TpShortUrlRepository;
import com.magicbricks.utils.Constants.UrlType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

	private static final String SLS = "/";

	private static final int MAX_RETRY = 5;

	@Autowired
	private TpShortUrlRepository repository;

	@Autowired
	private ShortCodeGenerator codeGenerator;

	private String domain;

	private UrlType type;

	private boolean preserveCode;

	@Override
	public Mono<ShortUrlDto> getShortUrl(final ShortUrlRequest request) {
		this.preserveCode = request.isPreserve();
		final String fullUrl = request.getFullUrl();
		this.domain = request.getDomain();
		this.type = request.getType();
		return repository.getByFullUrl(fullUrl)
//				.map(this::updateModidate)
				.switchIfEmpty(Mono.defer(() -> createShortUrl(fullUrl)))
				.retry(MAX_RETRY)
				.map(this::mapToShortUrlDto);
	}

//	private TpshortUrl updateModidate(final TpshortUrl pojo) {
//		if (this.preserveCode) {
//			log.info("updating the short url object, as found for full url :{}", pojo.getFullurl());
//			pojo.setModdate(LocalDateTime.now());
//			repository.save(pojo).subscribe();
//		}
//		return pojo;
//	}

	private Mono<? extends TpshortUrl> createShortUrl(final String fullUrl) {
		log.info("creating short url object, as not found for full url :{}", fullUrl);
		final String code = codeGenerator.getCode();
		log.info("sort code :{}", code);
		return repository.getByShortUrl(code)
				.map(this::throwErrorIfShortCodeExist)
				.switchIfEmpty(createShortUrlInDb(code, fullUrl));
	}

	private Mono<TpshortUrl> createShortUrlInDb(final String code, final String fullUrl) {
		log.info("Saving into db for full url:{}", fullUrl);
		final TpshortUrl pojo = createShortUrlObject(code, fullUrl);
		return repository.save(pojo);
	}

	private TpshortUrl throwErrorIfShortCodeExist(final TpshortUrl pojo) {
		if (pojo != null) {
			log.error("Object found for code :{}", pojo.getShorturl());
			throw new ShortCodeExistExceptio("Short URL already exist for the URL");
		}
		return pojo;
	}

	private ShortUrlDto mapToShortUrlDto(final TpshortUrl pojo) {
		final String shortUrl = normalizeShortURL(pojo);
		return ShortUrlDto.builder()
				.shortUrl(shortUrl)
				.fullUrl(pojo.getFullurl())
				.id(pojo.getShorturlfnum())
				.build();
	}

	private String normalizeShortURL(final TpshortUrl pojo) {
		String shortUrl = null;
		try {
			final String uriStr = this.domain + SLS + this.type.get() + SLS
					+ codeGenerator.getEncodedCode(pojo.getShorturl());
			shortUrl = new URI(uriStr).normalize().toString();
		} catch (URISyntaxException e) {
			log.error("Exception in normalizing short url");
			throw new RuntimeException("Exception in normalizing short url");
		}
		return shortUrl;
	}

	private TpshortUrl createShortUrlObject(final String code, final String fullUrl) {
		final TpshortUrl pojo = new TpshortUrl();
		pojo.setShorturl(code);
		pojo.setFullurl(fullUrl);
		pojo.setCreatedby(0l);
		pojo.setCreatedate(LocalDateTime.now());
		pojo.setModdate(LocalDateTime.now());
		log.info("Preserving this code :{}", this.preserveCode);
		// preserve will be used here to preserve this sort code
		return pojo;
	}
}

package com.magicbricks.service;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magicbricks.entity.TpshortUrl;
import com.magicbricks.exceptions.ShortUrlNotFoundException;
import com.magicbricks.exceptions.UrlTypeException;
import com.magicbricks.repositories.TpShortUrlRepository;
import com.magicbricks.utils.Constants.UrlType;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RedirectorServiceImpl implements RedirectorService {

	@Autowired
	private TpShortUrlRepository repository;

	@Autowired
	private ShortCodeGenerator codeGenerator;

	@Override
	public Mono<String> getUrlByShortCode(String code) {

		log.info("Encoded short code :{}", code);
		final String decoded = codeGenerator.getDcodedCode(code);

		log.info("Decode short coe :{}", decoded);
		if (StringUtils.isBlank(decoded)) {
			throw new ShortUrlNotFoundException("Short URL not found");
		}
		final Mono<TpshortUrl> shortUrl = repository.getByShortUrl(decoded);
		return shortUrl
				.map(this::mapToFullUrl);
	}

	private String mapToFullUrl(final TpshortUrl url) {
		if (url == null) {
			throw new ShortUrlNotFoundException("Short URL not found");
		}
		return url.getFullurl();
	}

	@Override
	public boolean validateUrlType(String urlTye) throws UrlTypeException {
		if (StringUtils.isBlank(urlTye) || urlTye.trim().length() > 1) {
			throw new UrlTypeException("Invalid url type");
		}
		final List<UrlType> enums = EnumUtils.getEnumList(UrlType.class);
		final UrlType data = enums
				.stream().filter(e -> e.get().equals(urlTye))
				.findFirst()
				.orElse(null);
		if (data == null) {
			throw new UrlTypeException("Invalid url type");
		}
		return true;
	}
}

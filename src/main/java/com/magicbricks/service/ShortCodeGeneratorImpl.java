package com.magicbricks.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.magicbricks.utils.EncDec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShortCodeGeneratorImpl implements ShortCodeGenerator {

	@Autowired
	private EncDec encDec;

	private static final int DEFAULT_LENGTH = 6;
	@Value("${short.code.length:6}")
	private Integer length;

	@Override
	public String getCode() {
		Integer codeLegth = length;
		if (length == null || length == 0) {
			log.info("Setting default code length to :{}", DEFAULT_LENGTH);
			codeLegth = DEFAULT_LENGTH;
		}
		log.info("Getting {} code length code", codeLegth);
		return RandomStringUtils.randomAlphanumeric(codeLegth);
	}

	@Override
	public String getEncodedCode(final String code) {
		if (StringUtils.isBlank(code)) {
			throw new RuntimeException("Code can not be null or empty");
		}
		final String ecrypted = encDec.encrypt(code);
		final String encoded = URLEncoder.encode(ecrypted, Charset.defaultCharset());
		return encoded;
	}

	@Override
	public String getDcodedCode(final String code) {
		if (StringUtils.isBlank(code)) {
			throw new RuntimeException("Code can not be null or empty");
		}
		final String decoded = URLDecoder.decode(code, Charset.defaultCharset());
		final String decrypted = encDec.decrypt(decoded);
		return decrypted;
	}
}

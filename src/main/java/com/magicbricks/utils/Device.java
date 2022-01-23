package com.magicbricks.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Component
public class Device {

	private static final String USER_AGENT = "User-Agent";

	@SuppressWarnings("deprecation")
	public boolean isMobile(final ServerHttpRequest request) {
		final UserAgent userAgent = getUserAgent(request);
		return userAgent.getOperatingSystem().isMobileDevice();
	}

	public OperatingSystem getOperatingSystem(final ServerHttpRequest request) {
		final UserAgent userAgent = getUserAgent(request);
		return userAgent.getOperatingSystem();
	}

	private UserAgent getUserAgent(final ServerHttpRequest request) {
		final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeaders().getFirst(USER_AGENT));
		return userAgent;
	}
}

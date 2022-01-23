package com.magicbricks.exceptions;

public class ShortUrlNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ShortUrlNotFoundException(final String msg) {
		super(msg);
	}
}

package com.magicbricks.service;

public interface ShortCodeGenerator {

	String getCode();

	String getEncodedCode(String code);

	String getDcodedCode(String code);

}
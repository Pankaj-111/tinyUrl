package com.magicbricks.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import com.magicbricks.utils.Constants.UrlType;

import lombok.Data;

@Data
public class ShortUrlRequest {
	@NotBlank(message = "The url is mandetory")
	@URL(message = "Invalid URL")
	private String fullUrl;

	private String domain = "http://localhost:8081/";

	@NotNull
	private UrlType type;

	private boolean preserve = false;
}

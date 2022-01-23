package com.magicbricks.utils;

public class Constants {
	public enum UrlType {
		PROMO("g"), PRIORITY("p");
		private String value;
		private UrlType(String value) {
			this.value=value;
		}
		public String get() {
			return this.value;
		}
	}
}

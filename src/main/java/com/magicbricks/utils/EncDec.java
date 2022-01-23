package com.magicbricks.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EncDec {
	private static final String KEYGEN_STR = "23435356677";
	private static final String PADDING = "DES/ECB/PKCS5Padding";
	private static final String UTF8 = "UTF-8";

	private Key getKey() {
		try {
			byte[] bytes = getbytes(KEYGEN_STR);
			DESKeySpec pass = new DESKeySpec(bytes);
			SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey sKey = sKeyFactory.generateSecret(pass);
			return sKey;
		} catch (Exception ex) {
			log.error("Exception in getKey", ex);
		}
		return null;
	}

	private Key getKey(String encKey) {
		try {
			byte[] bytes = getbytes(encKey);
			DESKeySpec pass = new DESKeySpec(bytes);
			SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey sKey = sKeyFactory.generateSecret(pass);
			return sKey;
		} catch (Exception ex) {
			log.error("Exception in getKey", ex);
		}
		return null;
	}

	private byte[] getbytes(String str) {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		StringTokenizer sTokenizer = new StringTokenizer(str, "-", false);
		while (sTokenizer.hasMoreTokens()) {
			try {
				byteOutputStream.write(sTokenizer.nextToken().getBytes());
			} catch (IOException ex) {
				log.error("IOException in getbytes", ex);
			}
		}
		byteOutputStream.toByteArray();
		return byteOutputStream.toByteArray();
	}

	public String encryptNonStatic(String sourceStr) {
		try {
			// Get secret key
			Key key = getKey();
			byte[] enc;
			Cipher ecipher1 = Cipher.getInstance(PADDING);
			ecipher1.init(Cipher.ENCRYPT_MODE, key);
			enc = ecipher1.doFinal(sourceStr.getBytes(UTF8));
			// Encode bytes to base64 to get a string
			// return new sun.misc.BASE64Encoder().encode(enc);
			return new org.apache.commons.codec.binary.Base64().encodeAsString(enc);
		} catch (Exception ex) {
			log.error("[Exception [EncDec : while encrypting the string]]");
		}
		return null;
	}

	public String encrypt(String sourceStr) {
		try {
			// Get secret key
			Key key = getKey();
			byte[] enc;
			Cipher ecipher = Cipher.getInstance(PADDING);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			enc = ecipher.doFinal(sourceStr.getBytes(UTF8));
			// Encode bytes to base64 to get a string
			Pattern whileSpace = Pattern.compile("\\s+", Pattern.MULTILINE);
			String encryptedString = new org.apache.commons.codec.binary.Base64().encodeAsString(enc);// new
																										// sun.misc.BASE64Encoder().encode(enc);

			return whileSpace.matcher(encryptedString).replaceAll("");
		} catch (Exception ex) {
			log.error("[Exception in encrypt", ex);
		}
		return null;
	}

	public String encrypt(String sourceStr, String encKey) {
		try {
			// Get secret key
			Key key = getKey(encKey);
			byte[] enc;
			Cipher ecipher = Cipher.getInstance(PADDING);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			enc = ecipher.doFinal(sourceStr.getBytes(UTF8));
			// Encode bytes to base64 to get a string
			Pattern whileSpace = Pattern.compile("\\s+", Pattern.MULTILINE);
			String encryptedString = new org.apache.commons.codec.binary.Base64().encodeAsString(enc);// new
																										// sun.misc.BASE64Encoder().encode(enc);
			return whileSpace.matcher(encryptedString).replaceAll("");
		} catch (Exception ex) {
			log.error("[Exception in encrypt", ex);
		}
		return null;
	}

	public String decryptNonStatic(String sourceStr) {
		if (sourceStr == null || "".equals(sourceStr)) {
			return null;
		}
		try {
			// Get secret key
			sourceStr = sourceStr.replace(' ', '+');
			sourceStr = sourceStr.replace("%20", "+");
			sourceStr = sourceStr.replaceAll("\n", "");

			Key key = getKey();
			byte[] dec;
			Cipher dcipher = Cipher.getInstance(PADDING);
			dcipher.init(Cipher.DECRYPT_MODE, key);
			// Decode base64 to get bytes
			// dec = new sun.misc.BASE64Decoder().decodeBuffer(sourceStr);
			dec = Base64.decodeBase64(sourceStr);
			// Decrypt data in a single step
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, UTF8);
		} catch (Exception ex) {
			log.error(
					"javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher");
		}
		return null;
	}

	public String decryptDynamic(String sourceStr, String keyStr) {
		try {
			// Get secret key
			sourceStr = sourceStr.replace(' ', '+');
			sourceStr = sourceStr.replaceAll("\n", "");
			sourceStr = sourceStr.replace("%20", "+");

			Key key = getKey(keyStr);
			byte[] dec;
			Cipher dcipher = Cipher.getInstance(PADDING);
			dcipher.init(Cipher.DECRYPT_MODE, key);
			// Decode base64 to get bytes
			dec = Base64.decodeBase64(sourceStr);
			// Decrypt data in a single step
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, UTF8);
		} catch (Exception ex) {
			// log.info("Can not decrypt the string ::"+sourceStr+": Exception thrown
			// :"+ex);
		}
		return null;
	}

	public String decrypt(String sourceStr) {
		try {
			// Get secret key
			sourceStr = sourceStr.replace(' ', '+');
			sourceStr = sourceStr.replaceAll("\n", "");
			sourceStr = sourceStr.replace("%20", "+");

			Key key = getKey();
			byte[] dec;
			Cipher dcipher = Cipher.getInstance(PADDING);
			dcipher.init(Cipher.DECRYPT_MODE, key);
			// Decode base64 to get bytes
			dec = Base64.decodeBase64(sourceStr);
			// Decrypt data in a single step
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, UTF8);
		} catch (Exception ex) {
			log.info("Can not decrypt the string ::" + sourceStr + ": Exception thrown :" + ex);
		}
		return null;
	}

	public String decrypt(String sourceStr, String encKey) {
		try {
			// Get secret key
			sourceStr = sourceStr.replace(' ', '+');
			sourceStr = sourceStr.replaceAll("\n", "");
			sourceStr = sourceStr.replace("%20", "+");

			Key key = getKey(encKey);
			byte[] dec;
			Cipher dcipher = Cipher.getInstance(PADDING);
			dcipher.init(Cipher.DECRYPT_MODE, key);
			// Decode base64 to get bytes
			dec = Base64.decodeBase64(sourceStr);
			// Decrypt data in a single step
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, UTF8);
		} catch (Exception ex) {
			log.info("Can not decrypt the string ::" + sourceStr + ": Exception thrown :" + ex);
		}
		return null;
	}

	public String encodePartialMobile(String mobile) {
		if (StringUtils.hasLength(mobile) && mobile.length() > 4)
			try {
				StringBuffer encodeMobileStr = new StringBuffer(mobile.substring(0, 2));
				for (int i = 0; i < mobile.length() - 4; i++) {
					encodeMobileStr.append("*");
				}
				encodeMobileStr.append(mobile.substring(mobile.length() - 2));
				return encodeMobileStr.toString();
			} catch (Exception ex) {
				log.info("Can not decrypt the string ::" + mobile + ": Exception thrown :" + ex);
			}
		return null;
	}
}

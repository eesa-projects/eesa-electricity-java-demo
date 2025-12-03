package com.eesa.eesa_electricity.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApiTestUtil {

	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String generateNonce() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
		}
		return sb.toString();
	}

	public static String generateSign(String appId, String timestamp, String nonce, String appSecret) {
		try {
			String originSign = String.format("appId=%s&appSecret=%s&nonce=%s&timestamp=%s",
					appId, appSecret != null ? appSecret : "", nonce, timestamp);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(originSign.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("生成签名失败", e);
		}
	}

	public static Map<String, String> buildAuthHeaders(String appId, String appSecret) {
		if (appId == null) {
			throw new IllegalArgumentException("appId cannot be null");
		}
		long timestamp = System.currentTimeMillis();
		String nonce = generateNonce();
		String secret = appSecret != null ? appSecret : "";
		String sign = generateSign(appId, String.valueOf(timestamp), nonce, secret);

		Map<String, String> headers = new HashMap<>();
		headers.put("appId", appId);
		headers.put("timestamp", String.valueOf(timestamp));
		headers.put("sign", sign);
		headers.put("nonce", nonce);

		return headers;
	}

	public static Map<String, String> buildAuthHeaders(String appId) {
		return buildAuthHeaders(appId, "");
	}
}


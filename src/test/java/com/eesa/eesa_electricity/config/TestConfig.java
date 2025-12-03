package com.eesa.eesa_electricity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application-test.properties", "classpath:application-local.properties"}, ignoreResourceNotFound = true)
public class TestConfig {

	@Value("${test.base.url}")
	private String baseUrl;

	@Value("${test.app.id}")
	private String appId;

	@Value("${test.app.secret}")
	private String appSecret;

	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@Value("${spring.datasource.username}")
	private String datasourceUsername;

	@Value("${spring.datasource.password}")
	private String datasourcePassword;

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public String getDatasourceUrl() {
		return datasourceUrl;
	}

	public String getDatasourceUsername() {
		return datasourceUsername;
	}

	public String getDatasourcePassword() {
		return datasourcePassword;
	}
}


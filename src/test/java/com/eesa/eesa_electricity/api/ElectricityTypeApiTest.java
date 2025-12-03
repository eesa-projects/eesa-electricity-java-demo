package com.eesa.eesa_electricity.api;

import com.eesa.eesa_electricity.config.TestConfig;
import com.eesa.eesa_electricity.util.ApiTestUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:application-local.properties"})
public class ElectricityTypeApiTest {

	@Autowired
	private TestConfig testConfig;

	@BeforeEach
	public void setUp() {
		assertNotNull(testConfig, "TestConfig should not be null");
		assertNotNull(testConfig.getBaseUrl(), "Base URL should not be null");
		assertNotNull(testConfig.getAppId(), "App ID should not be null");
		RestAssured.baseURI = testConfig.getBaseUrl();
		RestAssured.config = RestAssured.config()
				.sslConfig(io.restassured.config.SSLConfig.sslConfig().relaxedHTTPSValidation());
	}

	@Test
	public void testGetElectricityTypeVoltageLevel() {
		String regionName = "广东省珠三角五市";

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.queryParam("regionName", regionName)
				.when()
				.get("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getElectricityTypeVoltageLevel")
				.then()
				.statusCode(200)
				.body("resp_code", equalTo(0))
				.body("resp_msg", notNullValue())
				.body("timeStamp", notNullValue())
				.body("datas", notNullValue())
				.body("datas", instanceOf(List.class))
				.extract()
				.response();

		List<Map<String, Object>> datas = response.jsonPath().getList("datas");
		assertNotNull(datas);
		assertFalse(datas.isEmpty());

		for (Map<String, Object> data : datas) {
			assertTrue(data.containsKey("paramName"));
			assertTrue(data.containsKey("paramDesc"));
			assertTrue(data.containsKey("children"));
		}
	}

	@Test
	public void testGetElectricityTypeVoltageLevelWithoutRegionName() {
		given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.when()
				.get("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getElectricityTypeVoltageLevel")
				.then()
				.statusCode(anyOf(is(400), is(200)));
	}

	@Test
	public void testGetElectricityTypeVoltageLevelWithInvalidRegion() {
		String regionName = "不存在的地区";

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.queryParam("regionName", regionName)
				.when()
				.get("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getElectricityTypeVoltageLevel")
				.then()
				.statusCode(200)
				.extract()
				.response();

		int respCode = response.jsonPath().getInt("resp_code");
		assertTrue(respCode != 0 || response.jsonPath().getList("datas").isEmpty());
	}
}


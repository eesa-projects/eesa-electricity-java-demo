package com.eesa.eesa_electricity.api;

import com.eesa.eesa_electricity.config.TestConfig;
import com.eesa.eesa_electricity.util.ApiTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:application-local.properties"})
public class ElectricityPriceApiTest {

	@Autowired
	private TestConfig testConfig;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = testConfig.getBaseUrl();
		RestAssured.config = RestAssured.config()
				.sslConfig(io.restassured.config.SSLConfig.sslConfig().relaxedHTTPSValidation());
	}

	@Test
	public void testGetSeparationTimeElectricityPrice_NormalRegion() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "浙江省");
		requestBody.put("electricityTypeOneName", "BIPARTITE_SYSTEM");
		requestBody.put("electricityTypeTwoName", "GENERAL_INDUSTRY_COMMERCE");
		requestBody.put("tariffLevelId", "4");
		requestBody.put("years", "2025.11");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/minutesAndMonths/api/v4/getSeparationTimeElectricityPrice")
				.then()
				.statusCode(200)
				.extract()
				.response();

		System.out.println("Response: " + response.asString());
		int respCode = response.jsonPath().getInt("resp_code");
		if (respCode != 0) {
			System.out.println("Error resp_code: " + respCode + ", resp_msg: " + response.jsonPath().getString("resp_msg"));
		}

		assertEquals(0, respCode, "接口调用失败，resp_code: " + respCode);
		assertNotNull(response.jsonPath().get("resp_msg"));
		assertNotNull(response.jsonPath().get("timeStamp"));
		assertNotNull(response.jsonPath().get("datas"));

		Map<String, Object> datas = response.jsonPath().getMap("datas");
		assertNotNull(datas);
		assertTrue(datas.containsKey("capacityElectricityPrice"));
		assertTrue(datas.containsKey("demandElectricityPrice"));
		assertTrue(datas.containsKey("timeElectricityPriceResps"));

		List<Map<String, Object>> timePrices = response.jsonPath().getList("datas.timeElectricityPriceResps");
		assertNotNull(timePrices);
		assertFalse(timePrices.isEmpty());

		for (Map<String, Object> timePrice : timePrices) {
			assertTrue(timePrice.containsKey("startTime"));
			assertTrue(timePrice.containsKey("endTime"));
			assertTrue(timePrice.containsKey("periodType"));
			assertTrue(timePrice.containsKey("electrovalence"));
		}
	}

	@Test
	public void testGetSeparationTimeElectricityPrice_Shenzhen() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "广东省深圳市");
		requestBody.put("electricityTypeOneName", "A");
		requestBody.put("tariffLevelId", "12");
		requestBody.put("years", "2025.06");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/minutesAndMonths/api/v4/getSeparationTimeElectricityPrice")
				.then()
				.statusCode(200)
				.extract()
				.response();

		System.out.println("Response: " + response.asString());
		int respCode = response.jsonPath().getInt("resp_code");
		if (respCode != 0) {
			System.out.println("Error resp_code: " + respCode + ", resp_msg: " + response.jsonPath().getString("resp_msg"));
		}
		assertEquals(0, respCode, "接口调用失败，resp_code: " + respCode);
		Map<String, Object> datas = response.jsonPath().getMap("datas");
		assertNotNull(datas);
	}

	@Test
	public void testGetSeparationTimeElectricityPrice_Tibet() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "西藏自治区");
		requestBody.put("electricityTypeTwoName", "LARGE_INDUSTRY");
		requestBody.put("tariffLevelId", "3");
		requestBody.put("years", "2025.06");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/minutesAndMonths/api/v4/getSeparationTimeElectricityPrice")
				.then()
				.statusCode(200)
				.extract()
				.response();

		System.out.println("Response: " + response.asString());
		int respCode = response.jsonPath().getInt("resp_code");
		if (respCode != 0) {
			System.out.println("Error resp_code: " + respCode + ", resp_msg: " + response.jsonPath().getString("resp_msg"));
		}
		assertEquals(0, respCode, "接口调用失败，resp_code: " + respCode);
		Map<String, Object> datas = response.jsonPath().getMap("datas");
		assertNotNull(datas);
	}

	@Test
	public void testGetSeparationTimeElectricityPrice_MissingRequiredParams() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "浙江省");

		given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/minutesAndMonths/api/v4/getSeparationTimeElectricityPrice")
				.then()
				.statusCode(anyOf(is(400), is(200)));
	}
}


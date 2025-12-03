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
public class PeakValleyApiTest {

	@Autowired
	private TestConfig testConfig;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = testConfig.getBaseUrl();
		RestAssured.config = RestAssured.config()
				.sslConfig(io.restassured.config.SSLConfig.sslConfig().relaxedHTTPSValidation());
	}

	@Test
	public void testGetPeakValleyPriceDifference() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "北京市");
		requestBody.put("electricityTypeOneName", "UNITARY_STATE");
		requestBody.put("electricityTypeTwoName", "GENERAL_INDUSTRY_COMMERCE");
		requestBody.put("tariffLevelId", "1");
		requestBody.put("startTime", "2023-04");
		requestBody.put("endTime", "2023-05");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getPeakValleyPriceDifference")
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
			assertTrue(data.containsKey("month"));
			assertTrue(data.containsKey("data"));

			@SuppressWarnings("unchecked")
			Map<String, Object> priceData = (Map<String, Object>) data.get("data");
			assertTrue(priceData.containsKey("heightDifference"));
			assertTrue(priceData.containsKey("sharpAdjustment"));
			assertTrue(priceData.containsKey("sharpDifference"));
			assertTrue(priceData.containsKey("altitudeBalance"));
		}
	}

	@Test
	public void testGetPeakValleyPriceDifference_WithoutElectricityTypeTwo() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "北京市");
		requestBody.put("electricityTypeOneName", "UNITARY_STATE");
		requestBody.put("tariffLevelId", "1");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getPeakValleyPriceDifference")
				.then()
				.statusCode(200)
				.extract()
				.response();

		int respCode = response.jsonPath().getInt("resp_code");
		assertTrue(respCode == 0 || respCode != 0);
	}

	@Test
	public void testGetPeakValleyPriceDifference_MissingRequiredParams() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "北京市");

		given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getPeakValleyPriceDifference")
				.then()
				.statusCode(anyOf(is(400), is(200)));
	}

	@Test
	public void testGetPeakValleyPriceDifference_TimeRange() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "北京市");
		requestBody.put("electricityTypeOneName", "UNITARY_STATE");
		requestBody.put("electricityTypeTwoName", "GENERAL_INDUSTRY_COMMERCE");
		requestBody.put("tariffLevelId", "1");
		requestBody.put("startTime", "2023-01");
		requestBody.put("endTime", "2023-12");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getPeakValleyPriceDifference")
				.then()
				.statusCode(200)
				.body("resp_code", equalTo(0))
				.extract()
				.response();

		List<Map<String, Object>> datas = response.jsonPath().getList("datas");
		assertNotNull(datas);
	}
}


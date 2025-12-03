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
public class HolidayElectricityPriceApiTest {

	@Autowired
	private TestConfig testConfig;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = testConfig.getBaseUrl();
		RestAssured.config = RestAssured.config()
				.sslConfig(io.restassured.config.SSLConfig.sslConfig().relaxedHTTPSValidation());
	}

	@Test
	public void testGetHolidayElectricityPricesRegion() {
		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.when()
				.get("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityPricesRegion")
				.then()
				.statusCode(200)
				.body("resp_code", equalTo(0))
				.body("resp_msg", notNullValue())
				.body("timeStamp", notNullValue())
				.body("datas", notNullValue())
				.body("datas", instanceOf(List.class))
				.extract()
				.response();

		List<String> regions = response.jsonPath().getList("datas");
		assertNotNull(regions);
		assertFalse(regions.isEmpty());
	}

	@Test
	public void testGetHolidayElectricityTypeVoltageLevel() {
		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.queryParam("regionName", "上海市")
				.when()
				.get("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityTypeVoltageLevel")
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
	public void testGetHolidayElectricityPriceSelectMonth() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "上海市");
		requestBody.put("electricityTypeOneName", "BIPARTITE_SYSTEM");
		requestBody.put("electricityTypeTwoName", "LARGE_INDUSTRY");
		requestBody.put("tariffLevelId", "1");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityPriceSelectMonth")
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

		List<Map<String, Object>> datas = response.jsonPath().getList("datas");
		assertNotNull(datas);

		for (Map<String, Object> data : datas) {
			assertTrue(data.containsKey("paramName"));
			assertTrue(data.containsKey("paramDesc"));
		}
	}

	@Test
	public void testGetHolidayElectricityPrice() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "上海市");
		requestBody.put("electricityTypeOneName", "BIPARTITE_SYSTEM");
		requestBody.put("electricityTypeTwoName", "LARGE_INDUSTRY");
		requestBody.put("tariffLevelId", "1");
		requestBody.put("years", "2025.05");

		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityPrice")
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

		List<Map<String, Object>> datas = response.jsonPath().getList("datas");
		assertNotNull(datas);

		for (Map<String, Object> data : datas) {
			assertTrue(data.containsKey("festivalName"));
			assertTrue(data.containsKey("festivalStartTime"));
			assertTrue(data.containsKey("festivalEndTime"));
			assertTrue(data.containsKey("startTime"));
			assertTrue(data.containsKey("endTime"));
			assertTrue(data.containsKey("periodType"));
			assertTrue(data.containsKey("electrovalence"));
		}
	}

	@Test
	public void testGetHolidayElectricityPriceSelectMonth_MissingRequiredParams() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "上海市");

		given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityPriceSelectMonth")
				.then()
				.statusCode(anyOf(is(400), is(200)));
	}

	@Test
	public void testGetHolidayElectricityPrice_MissingRequiredParams() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("regionName", "上海市");

		given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/eesa-report/electricityPrice/holidayElectricityPrices/api/v1/getHolidayElectricityPrice")
				.then()
				.statusCode(anyOf(is(400), is(200)));
	}
}


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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties", "classpath:application-local.properties"})
public class RegionApiTest {

	@Autowired
	private TestConfig testConfig;

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = testConfig.getBaseUrl();
		RestAssured.config = RestAssured.config()
				.sslConfig(io.restassured.config.SSLConfig.sslConfig().relaxedHTTPSValidation());
	}

	@Test
	public void testGetRegionElectricityType() {
		Response response = given()
				.headers(ApiTestUtil.buildAuthHeaders(testConfig.getAppId(), testConfig.getAppSecret()))
				.when()
				.get("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getRegionElectricityType")
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
	public void testGetRegionElectricityTypeWithoutAuth() {
		Response response = given()
				.when()
				.get("/eesa-report/electricityPrice/electricityPricePublic/api/v4/getRegionElectricityType")
				.then()
				.statusCode(200)
				.body("resp_code", equalTo("1008"))
				.body("resp_msg", equalTo("参数有误"))
				.body("timeStamp", notNullValue())
				.body("datas", equalTo(null))
				.extract()
				.response();

		assertNull(response.jsonPath().getList("datas"));
	}
}


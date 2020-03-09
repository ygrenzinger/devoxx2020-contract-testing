package com.devoxx.inventory.contracts;

import com.devoxx.inventory.contracts.ContractsBase;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class ContractVerifierTest extends ContractsBase {

	@Test
	public void validate_shoudCreateBookIntoInventory() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"name\":\"QNWVDCWYUZNHIEOBQGBZ\",\"price\":57.53327066078319,\"stock\":1843673658}");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/v1/books");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['name']").isEqualTo("QNWVDCWYUZNHIEOBQGBZ");
			assertThatJson(parsedJson).field("['price']").isEqualTo(57.53327066078319);
			assertThatJson(parsedJson).field("['stock']").isEqualTo(1843673658);
	}

	@Test
	public void validate_shoudRetrieveAllBooks() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json");

		// when:
			ResponseOptions response = given().spec(request)
					.get("/v1/books");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).array().contains("['id']").isEqualTo("d4d37e73-77a0-4616-8bd2-5ed983d45d14");
			assertThatJson(parsedJson).array().contains("['name']").isEqualTo("Java");
			assertThatJson(parsedJson).array().contains("['price']").isEqualTo("19.9");
			assertThatJson(parsedJson).array().contains("['stock']").isEqualTo(100);
			assertThatJson(parsedJson).array().contains("['id']").isEqualTo("8364948b-6221-4cd8-9fd9-db0d17d45ef8");
			assertThatJson(parsedJson).array().contains("['name']").isEqualTo("Kotlin");
			assertThatJson(parsedJson).array().contains("['price']").isEqualTo("22.4");
			assertThatJson(parsedJson).array().contains("['stock']").isEqualTo(0);
	}

	@Test
	public void validate_shoudRetrieveBook() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json");

		// when:
			ResponseOptions response = given().spec(request)
					.get("/v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['name']").matches("[\\S\\s]+");
			assertThatJson(parsedJson).field("['price']").matches("-?(\\d*\\.\\d+)");
			assertThatJson(parsedJson).field("['stock']").matches("([1-9]\\d*)");
	}

	@Test
	public void validate_shouldreduceStockInInventory() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json");

		// when:
			ResponseOptions response = given().spec(request)
					.post("/v1/books/d4d37e73-77a0-4616-8bd2-5ed983d45d14/stock/reduce/5");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
			assertThatJson(parsedJson).field("['name']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['price']").matches("-?(\\d*\\.\\d+|\\d+)");
			assertThatJson(parsedJson).field("['stock']").matches("([1-9]\\d*)");
	}

}

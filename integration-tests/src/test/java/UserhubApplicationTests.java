import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.omriratson.userhub.UserhubApplication;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = UserhubApplication.class)
class UserhubApplicationTests {

	@BeforeAll
	static void datesHandler() {
		objectMapper = setupObjectMapper();
	}


	protected static ObjectMapper objectMapper;


	@Test
	void contextLoads() {
		final var body = Map.of(
				"email", "omri.ratson2@nice.com",
				"password", "#Eab2qzzFrzW@Ey$t#G^",
				"getUserDetails", true
		);

		var response = given()
				.contentType(JSON)
				.body(body)
				.post("https://na1.dev.nice-incontact.com/public/authentication/v1/login")
				.then()
				.contentType(JSON)
				.log()
				.all(true)
				.statusCode(HttpStatus.SC_OK)
				.extract()
				.response();

		System.out.println(response.getBody().prettyPrint());
	}

	public static ObjectMapper setupObjectMapper() {
		JavaTimeModule module = new JavaTimeModule();
//        LocalDateTimeDeserializer localDateTimeDeserializer = new
//                LocalDateTimeDeserializer(DATE_TIME_PATTERNS);
//        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
		return Jackson2ObjectMapperBuilder
				.json()
				.serializationInclusion(JsonInclude.Include.NON_EMPTY)
				.featuresToDisable(
						SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
						DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
				)
				.build();
	}

}

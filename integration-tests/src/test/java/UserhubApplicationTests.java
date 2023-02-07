import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.omriratson.userhub.UserhubApplication;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UserhubApplication.class)
class UserhubApplicationTests {
    @LocalServerPort
    private int port;

    @BeforeEach
    void datesHandler() {
        objectMapper = setupObjectMapper();
        RestAssured.port = port;
    }


    protected static ObjectMapper objectMapper;


    @Test
    void contextLoads() {
        final var body = """
                {
                	"username": "omri.ratson",
                	"password": "1234"
                }
                """;

        given()
                .contentType(JSON)
                .body(body)
                .post("/user-management/login")
                .then()
                .log()
                .all(true)
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

    }

    public static ObjectMapper setupObjectMapper() {
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

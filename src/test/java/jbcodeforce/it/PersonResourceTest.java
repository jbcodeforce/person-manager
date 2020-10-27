package jbcodeforce.it;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/health")
          .then()
             .statusCode(200)
             .body(containsString("UP"));
    }

}
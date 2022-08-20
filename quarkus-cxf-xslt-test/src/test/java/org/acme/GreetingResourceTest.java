package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testCase1() {
        given()
                .when().get("/test/hello1")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    public void testCase2() {
        given()
                .when().get("/test/hello2")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

}
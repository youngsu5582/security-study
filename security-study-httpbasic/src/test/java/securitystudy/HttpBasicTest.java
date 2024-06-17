package securitystudy;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpBasicTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void 없는_정보로_시도하면_failureUrl_로_리다이렉션한다() {
        RestAssured.given()
                .auth().preemptive().basic("user@naver.com", "password1234")
                .log().all()
                .get("/api/protected")
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    void some2(){
        RestAssured.given()
                .auth().preemptive().basic("user@naver.com", "password12345")
                .log().all()
                .get("/api/protected")
                .then()
                .log().all()
                .statusCode(401);
    }

}

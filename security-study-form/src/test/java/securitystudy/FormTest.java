package securitystudy;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FormTest {
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
                .formParam("email", "user@naver.com")
                .formParam("password", "password")
                .post("/login")
                .then()
                .statusCode(302)
                .header("Location", "http://localhost:" + port + "/login?error=true");
    }

    @Test
    void 로그인에_성공하면_successUrl_로_리다이렉션한다() {
        RestAssured.given()
                .formParam("email", "user@naver.com")
                .formParam("password", "password1234")
                .log().all()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .header("Location", "http://localhost:" + port + "/");
    }

    @Test
    void 인증_정보를_가지지_않은채_시도하면_403을_받는다() {
        RestAssured.given()
                .get("/api/protected")
                .then()
                .statusCode(403);
    }

    @Test
    void 인증_정보를_가지면_정상적으로_작동한다() {
        final String sessionId = RestAssured.given()
                .formParam("email", "user@naver.com")
                .formParam("password", "password1234")
                .post("/login")
                .getSessionId();

        RestAssured.given()
                .sessionId(sessionId)
                .get("/api/protected")
                .then()
                .statusCode(200);
    }
}

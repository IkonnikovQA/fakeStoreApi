package tests.swaggertests;

import assertions.Conditions;
import assertions.GenericAssertableResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import listener.CustomTpl;
import models.fakeapiuser.swagger.FullUser;
import models.fakeapiuser.swagger.Info;
import models.fakeapiuser.swagger.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import static io.restassured.RestAssured.given;


public class UserTests {
    private static Random random;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
    }

    @Test
    public void positiveRegisterTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);

       Assertions.assertEquals("User created", info.getMessage());
    }
    @Test
    public void negativeRegisterLoginExistsTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("User created", info.getMessage());

        Info errorInfo = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("Login already exist", errorInfo.getMessage());
    }
    @Test
    public void negativeRegisterNoPasswordTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("Missing login or password", info.getMessage());
    }

    @Test
    public void negativeRegisterNoPasswordGenericExampleTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .build();

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup").then();

        Info updated = new GenericAssertableResponse<Info>(response, Info.class)
                .should(Conditions.hasMessage("Missing login or password"))
                .should(Conditions.hasStatusCode(400))
                .asObject("info");

        Assertions.assertEquals("Missing login or password", updated.getMessage());
    }
    @Test
    public void positiveAdminAuthTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
    @Test
    public void positiveNewUserAuthTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
    @Test
    public void negativeAuthTest() {
        JwtAuthData authData = new JwtAuthData("asodn9121", "asd129j9zx");

        given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(401);
    }
    @Test
    public void positiveGetUserInfoTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);

        given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200);
    }
    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        given().auth().oauth2("some values")
                .get("/api/user")
                .then().statusCode(401);
    }
    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        given()
                .get("/api/user")
                .then().statusCode(401);
    }
    @Test
    public void positiveChangeUserPassTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Map<String, String> password = new HashMap<>();
        String updatedPassValue = "newPassUpdated";
        password.put("password", updatedPassValue);

        Info updatePassInfo = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then().extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User password successfully changed", updatePassInfo.getMessage());

        authData.setPassword(updatedPassValue);
        token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        FullUser updatedUser = given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);
        Assertions.assertNotEquals(user.getPass(), updatedUser.getPass());
    }
    @Test
    public void negativeChangeAdminPasswordTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Map<String, String> password = new HashMap<>();
        String updatedPassValue = "newPassUpdated";
        password.put("password", updatedPassValue);

        Info updatePassInfo = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant update base users", updatePassInfo.getMessage());
    }
    @Test
    public void negativeDeleteAdminTest() {
        JwtAuthData authData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Info info = given().auth().oauth2(token)
                .delete("/api/user")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant delete base users", info.getMessage());
    }

    @Test
    public void positiveDeleteNewUserTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData authData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Info infoDelete = given().auth().oauth2(token)
                .delete("/api/user")
                .then().statusCode(200)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User successfully deleted", infoDelete.getMessage());
    }@Test
    public void positiveGetAllUsersTest() {
        List<String> users = given()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<String>>() {
                });
        Assertions.assertTrue(users.size() >= 3);
    }


}

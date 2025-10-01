package services;

import io.restassured.http.ContentType;
import models.fakeapiuser.swagger.FullUser;
import models.fakeapiuser.swagger.Info;
import models.fakeapiuser.swagger.JwtAuthData;
import java.util.List;
import static io.restassured.RestAssured.given;

public class InsideUserService {

    public Info registerUser(FullUser user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
    }

    public String login(JwtAuthData authData) {
        return given()
                .contentType(ContentType.JSON)
                .body(authData)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
    }

    public Info updatePassword(String token, String newPassword) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("{\"password\":\"" + newPassword + "\"}")
                .put("/api/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
    }

    public Info deleteUser(String token) {
        return given()
                .auth().oauth2(token)
                .delete("/api/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
    }

    public FullUser getUserInfo(String token) {
        return given()
                .auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);
    }
}
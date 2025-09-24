package tests.swaggertests;

import assertions.Conditions;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import listener.AdminUser;
import listener.AdminUserResolver;
import listener.CustomTpl;
import models.fakeapiuser.swagger.FullUser;
import models.fakeapiuser.swagger.Info;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.UserService;
import java.util.List;
import static utils.RandomTestData.*;
@ExtendWith(AdminUserResolver.class)


public  class UserNewTests {
    private static UserService userService;
    private FullUser user;
    @BeforeEach
    public void initTestUser(){
        user = getRandomUser();
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        userService = new UserService();
    }
    @Test
    public void positiveRegisterTest() {
        userService.register(user)
                .should(Conditions.hasStatusCode(201))
                .should(Conditions.hasMessage("User created"));
    }

    @Test
    public void positiveRegisterWithGamesTest() {
        Response response = UserNewTests.userService.register(user)
                .should(Conditions.hasStatusCode(201))
                .should(Conditions.hasMessage("User created"))
                .asResponse();
        Info info = response.jsonPath().getObject("info", Info.class);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(info.getMessage())
                .as("Сообщение об ошибке было не верное")
                .isEqualTo("фейк меседж");

        softAssertions.assertThat(response.statusCode())
                .as("Статус код не был 200")
                .isEqualTo(201);
        softAssertions.assertAll();
    }

    @Test
    public void negativeRegisterLoginExistsTest() {
        userService.register(user);
        userService.register(user)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        user.setPass(null);
        userService.register(user)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Missing login or password"));
    }

    @Test
    public void positiveAdminAuthTest(@AdminUser FullUser admin) {
        String token = userService.auth(admin)
                .should(Conditions.hasStatusCode(200))
                .asJwt();
        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest() {
        userService.register(user);

        String token = userService.auth(user)
                .should(Conditions.hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest() {
        userService.auth(user)
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfoTest() {
        String token = userService.auth(user).asJwt();
        userService.getUserInfo(token)
                .should(Conditions.hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        userService.getUserInfo("fake jwt")
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        userService.getUserInfo()
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void positiveChangeUserPassTest() {
        String oldPassword = user.getPass();
        userService.register(user);

        String token = userService.auth(user).asJwt();

        String updatedPassValue = "newpassUpdated";

        userService.updatePass(updatedPassValue, token)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("User password successfully changed"));

        user.setPass(updatedPassValue);

        token = userService.auth(user).should(Conditions.hasStatusCode(200)).asJwt();

        FullUser updatedUser = userService.getUserInfo(token).as(FullUser.class);

        Assertions.assertNotEquals(oldPassword, updatedUser.getPass());
    }

    @Test
    public void negativeChangeAdminPasswordTest() {
        FullUser admin = getAdminUser();
        String token = userService.auth(admin).asJwt();
        String updatedPassValue = "newpassUpdated";
        userService.updatePass(updatedPassValue, token)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cant update base users"));
    }

    @Test
    public void negativeDeleteAdminTest() {
        FullUser admin = getAdminUser();
        String token = userService.auth(admin).asJwt();
        userService.deleteUser(token)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cant delete base users"));
    }

    @Test
    public void positiveDeleteNewUserTest() {
        userService.register(user);
        String token = userService.auth(user).asJwt();
        userService.deleteUser(token)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("User successfully deleted"));
    }

    @Test
    public void positiveGetAllUsersTest() {
        List<String> users = userService.getAllUsers().asList(String.class);
        Assertions.assertTrue(users.size() >= 3);
    }
}
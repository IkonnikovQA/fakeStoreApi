package tests.junit5.api;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import listener.CustomTpl;
import models.fakeapiuser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class SimpleApiRefactoredTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://fakestoreapi.com";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }
    @Test
    public void getAllUsersTest() {
        given().get("/users")
                .then()
                .statusCode(200);
    }
    @Test
    public void getSingleUserTest() {
        int userId = 5;
        UserRoot response = given()
                .pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 20})
    public void getAllUsersWithLimitTest(int limitSize) {
        List<UserRoot> users =  given().
                queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRoot>>(){});

        Assertions.assertEquals(limitSize, users.size());
    }
    @ParameterizedTest
    @ValueSource(ints = {0,40})
    public void getAllUsersWithErrorParams(int limitSize) {
        List<UserRoot> users =  given().
                queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRoot>>(){});

        Assertions.assertNotEquals(limitSize, users.size());
    }
    @Test
    public void getAllUsersSortByDescTest() {
        String sortType = "desc";

        List<UserRoot> usersSorted = given()
                .queryParam("sort", sortType)
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<UserRoot>>(){});

        List<UserRoot> usersNotSorted = given()
                .get("https://fakestoreapi.com/users")
                .then()
                .extract().as(new TypeRef<List<UserRoot>>(){});

        List<Integer> sortedResponseIds = usersSorted
                .stream()
                .map(UserRoot::getId).collect(Collectors.toList());

        List<Integer> sortedByCode = usersNotSorted
                .stream()
                .map(UserRoot::getId)
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        Assertions.assertNotEquals(usersSorted, usersNotSorted);
        Assertions.assertEquals(sortedResponseIds, sortedByCode);
    }
    private UserRoot getTestUser() {
        Random random = new Random();
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.3123", "81.1231");

        Address address = Address.builder()
                .city("Moscow")
                .number(random.nextInt(100))
                .zipcode("54231-4231")
                .street("Noviy Arbat 12")
                .geolocation(geolocation).build();

        return UserRoot.builder()
                .name(name)
                .phone("791237192")
                .email("fakemail@gmail.com")
                .username("thomasadmin")
                .password("mycoolpassword")
                .address(address).build();
    }
    @Test
    public void addNewUserTest() {
        UserRoot user = getTestUser();

        int userId = given().body(user)
                .post("/users")
                .then()
                .statusCode(201)
                .extract().jsonPath().getInt("id");

        Assertions.assertNotNull(userId);
    }
    @Test
    public void updateUserTest() {
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();
        user.setPassword("newpass111");

        UserRoot updatedUser = given()
                .body(user)
                .pathParam("userId", user.getId())
                .put("/users/{userId}")
                .then()
                .extract().as(UserRoot.class);

        Assertions.assertNotEquals(updatedUser.getPassword(), oldPassword);
    }
    @Test
    public void deleteUserTest() {
        given().delete("/users/7")
                .then()
                .statusCode(200);
    }
    @Test
    public void authUserTest() {
        AuthData authData = new AuthData("jimmie_k", "klein*#%*");

        String token =  given().contentType(ContentType.JSON)
                .body(authData)
                .post("/auth/login")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
}



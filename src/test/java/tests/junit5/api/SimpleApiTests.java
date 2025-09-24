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


public class SimpleApiTests {
    @Test
    public void getAllUsersTest(){
        given().get("https://fakestoreapi.com/users")
                .then()
                .log().all()
                .statusCode(200);

    }
    @Test
    public void getSingleUserTest() {
        int userId = 5;
        given().pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));
    }
    @Test
    public void getAllUsersWithLimitTest() {
        int limitSize = 3;
        given().queryParam("limit", limitSize)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .statusCode(200)
                .body("", hasSize(limitSize));
    }
    @Test
    public void getAllUsersSortByDescTest() {
        String sortType = "desc";

        Response sortedResponse = given().queryParam("sort", sortType)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();

        Response notSortedResponse = given()
                .get("https://fakestoreapi.com/users")
                .then().log().all().extract().response();

        List<Integer> sortedResponseIds = sortedResponse.jsonPath().getList("id");
        List<Integer> notSortedResponseIds = notSortedResponse.jsonPath().getList("id");

        List<Integer> sortedByCode = notSortedResponseIds
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertNotEquals(sortedResponseIds, notSortedResponseIds);
        Assertions.assertEquals(sortedByCode, sortedResponseIds);
    }
    @Test
    public void addNewUserTest() {
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.3123", "81.1231");

        Address address = Address.builder()
                .city("Moscow")
                .number(100)
                .zipcode("54231-4231")
                .street("Noviy Arbat 12")
                .geolocation(geolocation).build();

        UserRoot bodyRequest = UserRoot.builder()
                .name(name)
                .phone("791237192")
                .email("fakemail@gmail.com")
                .username("thomasadmin")
                .password("mycoolpassword")
                .address(address).build();

        given().body(bodyRequest)
                .post("https://fakestoreapi.com/users")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue());
    }
    private UserRoot getTestUser() {
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.3123", "81.1231");

        Address address = Address.builder()
                .city("Moscow")
                .number(100)
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
    public void updateUserTest() {
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();
        user.setPassword("newpass111");

        given().body(user)
                .put("https://fakestoreapi.com/users/" + user.getId())
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));
    }
    @Test
    public void deleteUserTest() {
        given().delete("https://fakestoreapi.com/users/7")
                .then().log().all()
                .statusCode(200);
    }
    @Test
    public void authUserTest() {
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("username", "jimmie_k");
        userAuth.put("password", "klein*#%*");

        given().contentType(ContentType.JSON)
                .body(userAuth)
                .post("https://fakestoreapi.com/auth/login")
                .then().log().all()
                .statusCode(201)
                .body("token", notNullValue());
    }


}



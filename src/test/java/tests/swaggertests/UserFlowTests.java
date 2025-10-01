package tests.swaggertests;

import flows.TestFlow;
import io.restassured.RestAssured;
import listener.CustomTpl;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserFlowTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }

    @Test
    public void shouldRegisterNewUser() {
        new TestFlow()
                .open()
                .register()
                .close();
    }

    @Test
    public void shouldChangeUserPassword() {
        new TestFlow()
                .open()
                .register()
                .changePassword()
                .close();
    }

    @Test
    public void shouldDeleteUser() {
        new TestFlow()
                .open()
                .register()
                .delete()
                .close();
    }
}
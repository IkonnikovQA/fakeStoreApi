package utils;

import models.fakeapiuser.swagger.FullUser;

import java.util.Random;

public class TestDataGenerator {
    private static final Random random = new Random();

    public static FullUser generateNewUser() {
        int randomNumber = Math.abs(random.nextInt());
        return FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();
    }
}
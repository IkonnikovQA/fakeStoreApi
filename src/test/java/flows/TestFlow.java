package flows;

import models.fakeapiuser.swagger.FullUser;
import models.fakeapiuser.swagger.Info;
import models.fakeapiuser.swagger.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import services.InsideUserService;
import utils.TestDataGenerator;

public class TestFlow {

    private final InsideUserService userService = new InsideUserService();
    private String token;
    private FullUser user;

    public TestFlow open() {return this;}

    public TestFlow register() {
        user = TestDataGenerator.generateNewUser();
        Info info = userService.registerUser(user);
        Assertions.assertEquals("User created", info.getMessage(), "User was not created successfully");

        token = userService.login(new JwtAuthData(user.getLogin(), user.getPass()));
        Assertions.assertNotNull(token, "Token is null after login");

        return this;
    }

    public TestFlow changePassword() {
        Info updateInfo = userService.updatePassword(token, "newPassUpdated");
        Assertions.assertEquals("User password successfully changed", updateInfo.getMessage());

        return this;
    }

    public TestFlow delete() {
        Info deleteInfo = userService.deleteUser(token);
        Assertions.assertEquals("User successfully deleted", deleteInfo.getMessage());

        return this;
    }

    public TestFlow close() {return this;}
}
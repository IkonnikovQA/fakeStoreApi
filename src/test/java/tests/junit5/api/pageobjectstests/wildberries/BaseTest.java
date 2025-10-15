package tests.junit5.api.pageobjectstests.wildberries;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest{
    protected WebDriver driver;

    @BeforeAll
    public static void downloadDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
       // options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }
}

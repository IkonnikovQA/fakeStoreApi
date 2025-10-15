package tests.junit5.api.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SeleniumTests {

    private static WebDriver driver;

    private static String downloadFolder = System.getProperty("user.dir") +
            File.separator + "build" +
            File.separator + "downloadFiles";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }
    @AfterAll
    public static void tearDown() {
        driver.close();
    }

    @Test
    public void simpleUiTest() {
        String expectedName = "Tomas Andersen";
        String expectedEmail = "tomas@gmail.com";
        String expectedCurrentAddress = "Moscow";
        String expectedPermanentAddress = "Rostov";


        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Text Box']"));
        elementsTextBox.click();

        WebElement fullName = driver.findElement(By.xpath("//input[@id='userName']"));
        WebElement email = driver.findElement(By.xpath("//input[@id='userEmail']"));
        WebElement currentAddress = driver.findElement(By.xpath("//textarea[@id='currentAddress']"));
        WebElement permanentAddress = driver.findElement(By.xpath("//textarea[@id='permanentAddress']"));
        WebElement submit = driver.findElement(By.xpath("//button[@id='submit']"));


        fullName.sendKeys(expectedName);
        email.sendKeys(expectedEmail);
        currentAddress.sendKeys(expectedCurrentAddress);
        permanentAddress.sendKeys(expectedPermanentAddress);
        submit.click();

        WebElement newName = driver.findElement(By.id("name"));
        WebElement newEmail = driver.findElement(By.id("email"));
        WebElement newCurrentAddress = driver.findElement(By.xpath("//div[@id='output']//p[@id='currentAddress']"));
        WebElement newPermanentAddress = driver.findElement(By.xpath("//div[@id='output']//p[@id='permanentAddress']"));

        String actualName = newName.getText();
        String actualEmail = newEmail.getText();
        String actualCurrentAddress = newCurrentAddress.getText();
        String actualPermanentAddress = newPermanentAddress.getText();

        Assertions.assertTrue(actualName.contains(expectedName));
        Assertions.assertTrue(actualEmail.contains(expectedEmail));
        Assertions.assertTrue(actualCurrentAddress.contains(expectedCurrentAddress));
        Assertions.assertTrue(actualPermanentAddress.contains(expectedPermanentAddress));
    }

    @Test
    public void uploadTest(){
        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();

        WebElement uploadBtn = driver.findElement(By.id("uploadFile"));
        uploadBtn.sendKeys(System.getProperty("user.dir") + "/src/test/resources/threadqa.jpeg");

        WebElement uploadedFakePath = driver.findElement(By.id("uploadedFilePath"));
        Assertions.assertTrue(uploadedFakePath.getText().contains("threadqa.jpeg"));
    }
    @Test
    public void testDownload(){
        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();

        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();

        WebElement downloadBtn = driver.findElement(By.id("downloadButton"));
        downloadBtn.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(x-> Paths.get(downloadFolder, "sticker.png").toFile().exists());

        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertNotNull(file);
        Assertions.assertTrue(file.length() > 0);

    }
    

    }


package tests.junit5.api.pageobjectstests.wildberries.wbpages.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import tests.junit5.api.pageobjectstests.wildberries.BasePage;

public class MainPage extends BasePage {

    private final By searchField = By.id("searchInput");
    private final By cartBtn= By.xpath("//a[@data-wba-header-name='Cart']");
    private final By loginBtn = By.xpath("//a[@data-wba-header-name='Login']");


    public MainPage(WebDriver driver) {
        super(driver);
        waitPageLoadsWb();
    }

    public SearchResultPage serchItem(String item){
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(item);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        return new SearchResultPage(driver);
    }

//    public LoginPage goToLogin(){
//        driver.findElement(loginBtn).click();
//    }

}

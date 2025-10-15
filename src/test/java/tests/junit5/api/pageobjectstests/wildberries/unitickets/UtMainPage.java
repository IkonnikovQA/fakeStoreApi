package tests.junit5.api.pageobjectstests.wildberries.unitickets;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.junit5.api.pageobjectstests.wildberries.BasePage;


public class UtMainPage extends BasePage {
    private By cityFromField = By.xpath("//input[@placeholder='Откуда']");
    private By listOfCityFrom = By.xpath("//input[@id='avia_form_origin-menu']");
    private By cityToField = By.xpath("//input[@placeholder='Куда']");
    private By listOfCityTo = By.xpath("//input[@id='avia_form_destination-menu']");
    private By dateForward = By.xpath("//button[@data-test-id='start-date-field']");
    private By dateBack = By.xpath("//button[@data-test-id='end-date-field']");
    private String dayInCalendar = "//td[@data-day='^\\d{4}-\\d{2}-\\d{2}$']";
    private By searchBtn = By.xpath("//button[@data-test-id='form-submit']");


    public UtMainPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(cityFromField));
        wait.until(ExpectedConditions.elementToBeClickable(searchBtn));
    }

    public UtMainPage setCityFrom(String city){
        driver.findElement(cityFromField).clear();
        driver.findElement(cityFromField).sendKeys(city);
        driver.findElement(cityFromField).click();
        waitForTextPresentedInList(listOfCityFrom, city).click();
        return this;
    }
    public UtMainPage setCityTo(String city){
        driver.findElement(cityToField).clear();
        driver.findElement(cityToField).sendKeys(city);
        driver.findElement(cityToField).click();
        waitForTextPresentedInList(listOfCityTo, city).click();
        return this;
    }

    public UtMainPage setDayForward(int day){
        driver.findElement(dateForward).click();
        getDay(day).click();
        wait.until(ExpectedConditions.invisibilityOf(getDay(day)));
        return this;
    }

    public UtMainPage setDayBack(int day){
        getDay(day).click();
        wait.until(ExpectedConditions.invisibilityOf(getDay(day)));
        return this;
    }

    private WebElement getDay(int day){
        By dayLocator = By.xpath(String.format(dayInCalendar, day));
        return driver.findElement(dayLocator);
    }

    public UtSearchPage search(){
        driver.findElement(searchBtn).click();
        return new UtSearchPage(driver);
    }

}

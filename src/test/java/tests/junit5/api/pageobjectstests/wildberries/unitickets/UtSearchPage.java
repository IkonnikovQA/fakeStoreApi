package tests.junit5.api.pageobjectstests.wildberries.unitickets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tests.junit5.api.pageobjectstests.wildberries.BasePage;

import java.util.List;

public class UtSearchPage extends BasePage {
    private By titleLoader = By.xpath("//div[@class='s__W3BjigGH1gQEnB5P']");
    private By priceSelectedMain = By.xpath("//div[@data-test-id='prices-ribbon-item-10']");
    private By selectedDay = By.xpath("//div[@data-test-id='text' and contains(text(), '24–31')]");
    private By listOfForwardDays = By.xpath("//span[@data-test-id='text' and contains(text(), '24')]");
    private By listOfBackDays = By.xpath("//span[@data-test-id='text' and contains(text(), '31')]");
    public UtSearchPage(WebDriver driver) {
        super(driver);
    }

    public List<Integer> getDaysForward() {
        return getDigitsFromList(listOfForwardDays);
    }
    public List<Integer> getDaysBack() {
        return getDigitsFromList(listOfBackDays);
    }
    public Integer getMainDayForward(){
        return getDigitFromWebElement(driver.findElement(selectedDay));
    }
    public void waitForPage(){
        waitForElementAppear(selectedDay);
        waitForTextMatchesRegex(priceSelectedMain, "\\d+");
    }
    public void waitForTitleDisappear(){
        waitForElementDisappear(titleLoader);
    }
}

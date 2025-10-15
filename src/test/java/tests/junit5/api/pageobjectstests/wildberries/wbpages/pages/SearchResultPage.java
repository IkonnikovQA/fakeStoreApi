package tests.junit5.api.pageobjectstests.wildberries.wbpages.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tests.junit5.api.pageobjectstests.wildberries.BasePage;

public class SearchResultPage extends BasePage {

    private By allFiltersBtn = By.xpath(".//button[@class='dropdown-filter__btn dropdown-filter__btn--all']");
    private By endPriceField = By.xpath("//input[@name='endN']");
    private By startPriceField = By.xpath("//input[@name='startN']");
    private By applyFilterBtn = By.xpath("//button[@class='filters-desktop__btn-main btn-main']");
    private By items = By.xpath("//div[@class='product-card-list']//article");

    public SearchResultPage (WebDriver driver) {
        super(driver);
    }

    public SearchResultPage openFilters(){
        driver.findElement(allFiltersBtn).click();
        return this;
    }

    public SearchResultPage setMinPrice(Integer minValue){
        driver.findElement(startPriceField).clear();
        driver.findElement(startPriceField).sendKeys(String.valueOf(minValue));
        return this;
    }

    public SearchResultPage setMaxPrice(Integer maxValue){
        clearTextFieldFull(endPriceField);
        driver.findElement(endPriceField).sendKeys(String.valueOf(maxValue));
        return this;
    }

    public SearchResultPage applyFilters(){
        driver.findElement(applyFilterBtn).click();
        waitForElementUpdated(items);
        return this;
    }

    public ItemPage openItem(){
        driver.findElements(items).get(0).click();
        waitPageLoadsWb();
        return new ItemPage(driver);
    }

}

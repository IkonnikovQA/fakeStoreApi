package tests.junit5.api.pageobjectstests.wildberries.wbpages.pages;

import org.openqa.selenium.*;
import tests.junit5.api.pageobjectstests.wildberries.BasePage;

public class ItemPage extends BasePage {

    private By itemHeaderName = By.xpath("//div[@class='product-page productPage--KE6FH abKtBlockDistance']");
    private By ItemPriceText = By.xpath("//span[@class='priceBlockPrice--xf8pi']");
    private By itemPrice = By.xpath("//span[@class='priceBlockPrice--xf8pi']");

    public ItemPage (WebDriver driver) {super(driver);}

    public String getItemName() {return driver.findElement(itemHeaderName).getText();}

    public Integer getItemPrice(){
        String priceText = getTextJs(itemPrice);
        priceText = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(priceText);
    }
}

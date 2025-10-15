package tests.junit5.api.pageobjectstests.wildberries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.junit5.api.pageobjectstests.wildberries.wbpages.pages.MainPage;
import tests.junit5.api.pageobjectstests.wildberries.wbpages.pages.ItemPage;

public class WsFilterTests extends BaseTest {
    @BeforeEach
    public void openSite(){
        driver.get("https://www.wildberries.ru/");
    }

    @Test
    public void searchResultTest(){
        String expectedItem = "iphone";
        Integer expectedPriceMax = 60000;
        Integer expectedPriceMin = 36000;

        ItemPage itemPage = new MainPage(driver)
                .serchItem(expectedItem)
                .openFilters()
                .setMinPrice(expectedPriceMin)
                .setMaxPrice(expectedPriceMax)
                .applyFilters()
                .openItem();

        String actualName = itemPage.getItemName();
        Integer actualPrice = itemPage.getItemPrice();

        Assertions.assertTrue(actualName.toLowerCase().contains(expectedItem.toLowerCase()));
        Assertions.assertTrue(actualPrice >= expectedPriceMin && actualPrice <= expectedPriceMax);

    }
}

package tests.junit5.api.pageobjectstests.wildberries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.junit5.api.pageobjectstests.wildberries.unitickets.UtMainPage;
import tests.junit5.api.pageobjectstests.wildberries.unitickets.UtSearchPage;

import java.util.List;

public class UtFilterTest extends BaseTest{

    @BeforeEach
    public void openSite(){
        driver.get("https://www.aviasales.ru/");
    }

    @Test
    public void filterTest(){
        int expectedDayForward = 24;
        int expectedDayBack = 26;

        UtSearchPage searchPage = new UtMainPage(driver).setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayForward(expectedDayForward)
                .setDayBack(expectedDayBack)
                .search();

        int actualDayForward = searchPage.getMainDayForward();
        //int actualDayBack = searchPage.getMainDayBack();
        Assertions.assertEquals(expectedDayForward, actualDayForward);
        //Assertions.assertEquals(expectedDayBack, actualDayBack);

        List<Integer> daysForward = searchPage.getDaysForward();
        List<Integer> daysBack = searchPage.getDaysBack();

        boolean isAllDaysForwardOk = daysForward.stream().allMatch(x->x.equals(expectedDayForward));
        boolean isAllDaysBackOk = daysBack.stream().allMatch(x->x.equals(expectedDayBack));
        Assertions.assertTrue(isAllDaysForwardOk);
        Assertions.assertTrue(isAllDaysBackOk);
    }
}

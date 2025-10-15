package tests.junit5.api.pageobjectstests.wildberries;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.junit5.api.pageobjectstests.wildberries.unitickets.UtMainSelenidePage;


public class UtSelenideTest {


    @Test
    public void firstSelenideTest(){
        int expectedDayForward = 24;
        int expectedDayBack = 30;
        Selenide.open("https://www.aviasales.ru/");
        UtMainSelenidePage mainPage = new UtMainSelenidePage();
        mainPage.setCityFrom("Казань")
                .setCityTo("Дубайй")
                .setDayForward(expectedDayForward)
                .setDayBack(expectedDayBack)
                .search()
                .waitForPage()
                .waitForTitleDisappear()
                .assertMainDayForward(expectedDayForward)
                .assertAllDaysBackShouldHaveDay(expectedDayBack)
                .assertAllDaysForwardShouldHaveDay(expectedDayForward);
    }
}

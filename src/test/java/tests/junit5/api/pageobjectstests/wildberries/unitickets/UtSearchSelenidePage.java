package tests.junit5.api.pageobjectstests.wildberries.unitickets;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class UtSearchSelenidePage {
    private SelenideElement titleLoader = $x("//div[@class='s__W3BjigGH1gQEnB5P']");
    private SelenideElement priceSelectedMain = $x("//div[@data-test-id='prices-ribbon-item-10']");
    private SelenideElement selectedDay = $x("//div[@data-test-id='text' and contains(text(), '24–31')]");
    private ElementsCollection listOfForwardDays = $$x("//span[@data-test-id='text' and contains(text(), '24')]");
    private ElementsCollection listOfBackDays = $$x("//span[@data-test-id='text' and contains(text(), '31')]");

    public UtSearchSelenidePage assertAllDaysForwardShouldHaveDay(int expectedForwardDay) {
        String day = String.valueOf(expectedForwardDay);
        listOfForwardDays.should(CollectionCondition.containExactTextsCaseSensitive(day));
        return this;
    }
    public UtSearchSelenidePage assertAllDaysBackShouldHaveDay(int expectedBackDay) {
        String day = String.valueOf(expectedBackDay);
        listOfBackDays.should(CollectionCondition.containExactTextsCaseSensitive(day));
        return this;
    }
    public UtSearchSelenidePage assertMainDayForward(int expectedDay){
       selectedDay.should(Condition.partialText(String.valueOf(expectedDay)));
       return this;
    }
    public UtSearchSelenidePage waitForPage(){
        priceSelectedMain.should(Condition.matchText("\\d+"));
        return this;
    }
    public UtSearchSelenidePage waitForTitleDisappear(){
        titleLoader.should(Condition.disappear, Duration.ofSeconds(30));
        return this;
    }

}

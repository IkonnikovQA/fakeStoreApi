package tests.junit5.api.pageobjectstests.wildberries.unitickets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;

public class UtMainSelenidePage {
    private SelenideElement cityFromField = $x("//input[@placeholder='Откуда']");
    private ElementsCollection listOfCityFrom = $$x("//section[@data-test-id='autocomplete-menu']//li[@data-test-id='suggested-city-KZN']");
    private SelenideElement cityToField = $x("//input[@placeholder='Куда']");
    private ElementsCollection listOfCityTo = $$x("//input[@id='avia_form_destination-menu']");
    private SelenideElement dateForward = $x("//button[@data-test-id='start-date-field']");
    private SelenideElement dateBack = $x("//button[@data-test-id='end-date-field']");
    private String dayInCalendar = "//td[@data-day='^\\d{4}-\\d{2}-\\d{2}$']";
    private SelenideElement searchBtn = $x("//button[@data-test-id='form-submit']");


    public UtMainSelenidePage setCityFrom(String city){
        cityFromField.clear();
        cityFromField.sendKeys(city);
        cityFromField.click();
        listOfCityFrom.find(Condition.partialText(city)).click();
        return this;
    }
    public UtMainSelenidePage setCityTo(String city){
        cityToField.clear();
        cityToField.sendKeys(city);
        cityToField.click();
        listOfCityTo.find((Condition.partialText(city))).click();
        return this;
    }

    public UtMainSelenidePage setDayForward(int day){
        dateForward.click();
        getDay(day).click();
        $x("//h2").click();
        return this;
    }

    public UtMainSelenidePage setDayBack(int day){
        getDay(day).click();
        $x("//h2").click();
        return this;
    }

    private SelenideElement getDay(int day){
        return  $x(String.format(dayInCalendar, day));
    }

    public UtSearchSelenidePage search(){
        searchBtn.click();
        return page(UtSearchSelenidePage.class);
    }
}
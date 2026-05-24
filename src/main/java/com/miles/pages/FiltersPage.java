package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class FiltersPage extends BasePage{

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Vans']")
    private WebElement carTypeVansLabel;

    @AndroidFindBy(accessibility = "qa_button_filters_fuel_level_100")
    private WebElement fuelLevelFull;

    public WebElement getCarTypeVansLabel() { return carTypeVansLabel; }
    public WebElement getFuelLevelFull()    { return fuelLevelFull; }
}

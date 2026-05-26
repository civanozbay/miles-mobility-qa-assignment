package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VehicleListPage extends BasePage{
    @AndroidFindBy(accessibility = "qa_vehicle_list_item_license_plate")
    private List<WebElement> vehicleItems;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Vehicles']")
    private WebElement vehiclesTitle;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Seems there are no cars available nearby')]")
    private WebElement noVehiclesPopupDescription;

    public int getVehicleCount() { return vehicleItems.size(); }
    public WebElement getVehiclesTitle()       { return vehiclesTitle; }
    public WebElement getNoVehiclesPopupDescription() { return noVehiclesPopupDescription; }
}

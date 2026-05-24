package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VehicleListPage extends BasePage{
    @AndroidFindBy(accessibility = "qa_vehicle_list_item_license_plate")
    private List<WebElement> vehicleItems;

    public int getVehicleCount() { return vehicleItems.size(); }
}

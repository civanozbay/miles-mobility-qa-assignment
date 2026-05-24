package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MapPage extends BasePage{

    @AndroidFindBy(xpath = "//*[@resource-id='qa_button_left_menu']")
    private WebElement leftMenuButton;

    @AndroidFindBy(accessibility = "qa_button_fab_help")
    private WebElement helpButton;

    @AndroidFindBy(accessibility = "qa_button_fab_filter")
    private WebElement filterButton;

    @AndroidFindBy(accessibility = "qa_button_fab_vehicle_list")
    private WebElement vehicleListButton;

    @AndroidFindBy(accessibility = "qa_button_fab_find_me")
    private WebElement findMeButton;

    @AndroidFindBy(id = "com.driveby.app:id/ucButtonText")
    private WebElement acceptCookiesBtn;

    @AndroidFindBy(accessibility = "qa_text_help_alert_dialog")
    private WebElement helpAlertDialog;

    public void acceptCookies() {
        wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesBtn)).click();
        waitForVisible(leftMenuButton);
    }

    public void tapLeftMenu()      { tap(leftMenuButton); }
    public void tapHelp()          { tap(helpButton); }
    public void tapFilter()        { tap(filterButton); }
    public void tapVehicleList()   { tap(vehicleListButton); }
    public void tapFindMe()        { tap(findMeButton); }

    public WebElement getLeftMenuButton()    { return leftMenuButton; }
    public WebElement getHelpButton()        { return helpButton; }
    public WebElement getFilterButton()      { return filterButton; }
    public WebElement getVehicleListButton() { return vehicleListButton; }
    public WebElement getFindMeButton()      { return findMeButton; }
    public WebElement getHelpAlertDialog()      { return helpAlertDialog; }

}

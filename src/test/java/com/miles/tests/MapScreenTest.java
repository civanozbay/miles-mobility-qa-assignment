package com.miles.tests;

import com.miles.base.BaseTest;
import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import com.miles.pages.FiltersPage;
import com.miles.pages.LoginEmailPage;
import com.miles.pages.MapPage;
import com.miles.pages.VehicleListPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MapScreenTest extends BaseTest {

    MapPage mapPage;


    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        mapPage = new MapPage();
        mapPage.acceptCookies();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }


    @Test
    public void leftMenuButton() {
        assertIsDisplayed(mapPage.getLeftMenuButton());
        mapPage.tapLeftMenu();
        LoginEmailPage loginEmailPage = new LoginEmailPage();
        Assert.assertEquals(loginEmailPage.getText(loginEmailPage.getWelcomeHeader()),TextConstants.LOGIN_EMAIL_WELCOME_HEADER);
        assertAppIsRunning();
    }

    @Test
    public void helpButton() {
        assertIsDisplayed(mapPage.getHelpButton());
        Assert.assertEquals(mapPage.getText(mapPage.getHelpButtonLabel()), TextConstants.MAP_HELP_BUTTON_LABEL);
        mapPage.tapHelp();
        Assert.assertEquals(mapPage.getText(mapPage.getHelpAlertDialog()), TextConstants.MAP_HELP_DIALOG_TITLE);
        assertAppIsRunning();
    }

    @Test
    public void filterButton() {
        assertIsDisplayed(mapPage.getFilterButton());
        mapPage.tapFilter();
        FiltersPage filtersPage = new FiltersPage();
        Assert.assertEquals(filtersPage.getText(filtersPage.getCarTypeVansLabel()), TextConstants.FILTERS_CAR_TYPE_VANS);
        assertIsDisplayed(filtersPage.getFuelLevelFull());
        assertAppIsRunning();
    }

    @Test
    public void vehicleListButton() {
        assertIsDisplayed(mapPage.getVehicleListButton());
        mapPage.tapVehicleList();
        VehicleListPage vehicleListPage = new VehicleListPage();
        Assert.assertEquals(
                vehicleListPage.getText(vehicleListPage.getVehiclesTitle()),
                TextConstants.VEHICLE_LIST_TITLE);

        if (vehicleListPage.getVehicleCount() > 0) {
            Assert.assertTrue(vehicleListPage.getVehicleCount() > 0);
        } else {
            Assert.assertEquals(
                    vehicleListPage.getText(vehicleListPage.getNoVehiclesPopupDescription()),
                    TextConstants.VEHICLE_LIST_NO_CARS_DESCRIPTION);
        }
        assertAppIsRunning();
    }

    @Test
    public void findMeButton() {
        assertIsDisplayed(mapPage.getFindMeButton());
        mapPage.tapFindMe();
        assertIsDisplayed(mapPage.getGoogleMapView());
        assertIsDisplayed(mapPage.getFilterButton());
        assertIsDisplayed(mapPage.getVehicleListButton());
        assertAppIsRunning();
    }
}

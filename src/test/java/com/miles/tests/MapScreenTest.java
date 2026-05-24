package com.miles.tests;

import com.miles.base.BaseTest;
import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import com.miles.pages.FiltersPage;
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
        assertAppIsRunning();
    }

    @Test
    public void helpButton() {
        assertIsDisplayed(mapPage.getHelpButton());
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
    public void vehicleListButton() throws InterruptedException {
        assertIsDisplayed(mapPage.getVehicleListButton());
        mapPage.tapVehicleList();
        VehicleListPage vehicleListPage = new VehicleListPage();
        Assert.assertTrue(vehicleListPage.getVehicleCount() > 0);
        assertAppIsRunning();
    }

    @Test
    public void findMeButton() {
        assertIsDisplayed(mapPage.getFindMeButton());
        mapPage.tapFindMe();
        assertAppIsRunning();
    }
}

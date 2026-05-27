package com.miles.base;

import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    @BeforeSuite
    public void startAppium() {
        DriverManager.startAppium();
    }

    @AfterSuite
    public void stopAppium() {
        DriverManager.stopAppium();
    }

    public void assertAppIsRunning(){
        Assert.assertEquals(DriverManager.getDriver().getCurrentPackage(), TextConstants.APP_PACKAGE);
    }

    public void assertIsDisplayed(WebElement element) {
        Assert.assertTrue(element.isDisplayed());
    }
}

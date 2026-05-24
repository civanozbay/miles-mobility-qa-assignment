package com.miles.base;

import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class BaseTest {

    public void assertAppIsRunning(){
        Assert.assertEquals(DriverManager.getDriver().getCurrentPackage(), TextConstants.APP_PACKAGE);
    }

    public void assertIsDisplayed(WebElement element) {
        Assert.assertTrue(element.isDisplayed());
    }
}

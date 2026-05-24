package com.miles.tests;

import com.miles.base.BaseTest;
import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import com.miles.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @Test
    public void negativeLogin() {

    }
}

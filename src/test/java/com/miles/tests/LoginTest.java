package com.miles.tests;

import com.miles.base.BaseTest;
import com.miles.constants.TextConstants;
import com.miles.driver.DriverManager;
import com.miles.pages.LoginEmailPage;
import com.miles.pages.LoginPasswordPage;
import com.miles.pages.MapPage;
import com.miles.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

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
    public void negativeLogin() {
        String email = ConfigReader.getInstance().get("test.email");
        String password = ConfigReader.getInstance().get("test.password");

        mapPage.tapLeftMenu();

        LoginEmailPage emailPage = new LoginEmailPage();
        Assert.assertEquals(
                emailPage.getText(emailPage.getWelcomeHeader()),
                TextConstants.LOGIN_EMAIL_WELCOME_HEADER);
        emailPage.enterEmail(email);
        emailPage.tapContinue();

        LoginPasswordPage passwordPage = new LoginPasswordPage();
        String accountFoundText = passwordPage.getText(passwordPage.getAccountFoundText());
        Assert.assertTrue(accountFoundText.contains(TextConstants.LOGIN_PASSWORD_ACCOUNT_FOUND));
        Assert.assertTrue(accountFoundText.contains(email));

        passwordPage.enterPassword(password);
        passwordPage.tapLogin();
        Assert.assertTrue(
                passwordPage.getText(passwordPage.getErrorMessage())
                        .contains(TextConstants.LOGIN_PASSWORD_ERROR));
    }
}

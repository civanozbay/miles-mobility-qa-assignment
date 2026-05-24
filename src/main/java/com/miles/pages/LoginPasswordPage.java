package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LoginPasswordPage extends BasePage{

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='LOGIN_PASSWORD_PASSWORD_INPUT']")
    private WebElement passwordInput;

    @AndroidFindBy(accessibility = "LOGIN_PASSWORD_SIGNIN_BTN")
    private WebElement loginButton;

    @AndroidFindBy(accessibility = "LOGIN_PASSWORD_PASSWORD_INPUT_ERROR")
    private WebElement errorMessage;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'We found your account')]")
    private WebElement accountFoundText;

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void tapLogin() {
        tap(loginButton);
    }

    public WebElement getErrorMessage()     { return errorMessage; }
    public WebElement getAccountFoundText() { return accountFoundText; }

}

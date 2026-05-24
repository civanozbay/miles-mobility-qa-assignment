package com.miles.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LoginEmailPage extends BasePage {

    @AndroidFindBy(accessibility = "qa_text_input_login_email")
    private WebElement emailInput;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id='LOGIN_EMAIL_BTN']")
    private WebElement continueButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"LET’S GET YOU ON THE ROAD\"]")
    private WebElement welcomeHeader;

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public WebElement getWelcomeHeader() { return welcomeHeader;}

    public void tapContinue() {
        tap(continueButton);
    }
}

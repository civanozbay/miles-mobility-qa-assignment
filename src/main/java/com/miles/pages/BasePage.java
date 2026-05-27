package com.miles.pages;

import com.miles.driver.DriverManager;
import com.miles.utils.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public  abstract class BasePage {

    AndroidDriver driver;
    WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        int timeout = ConfigReader.getInstance().getInt("explicit.wait");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void tap(WebElement element) {
        waitForVisible(element).click();
    }

    public void type(WebElement element, String text) {
        WebElement el = waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(WebElement element) {
        return waitForVisible(element).getText();
    }
}

package com.miles.driver;

import com.miles.utils.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static AndroidDriver driver;

    public static void initDriver() {
        ConfigReader config = ConfigReader.getInstance();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.get("platform.name"))
                .setAutomationName(config.get("automation.name"))
                .setDeviceName(config.get("device.name"))
                .setAppPackage(config.get("app.package"))
                .setAppActivity(config.get("app.activity"))
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        if (Boolean.parseBoolean(config.get("emulator"))) {
            options.setAvd(config.get("avd.name"))
                    .setAvdLaunchTimeout(Duration.ofSeconds(300))
                    .setAvdReadyTimeout(Duration.ofSeconds(300));
        }

        try {
            URL serverUrl = new URL(config.get("appium.url"));
            driver = new AndroidDriver(serverUrl, options);
            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(config.getInt("implicit.wait")));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL: " + config.get("appium.url"), e);
        }
    }
}

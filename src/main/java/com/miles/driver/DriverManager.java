package com.miles.driver;

import com.miles.utils.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static AndroidDriver driver;
    private static AppiumDriverLocalService appiumService;

    private DriverManager() {}

    public static void startAppium() {
        try {
            URL appiumUrl = new URL(ConfigReader.getInstance().get("appium.url"));
            appiumService = new AppiumServiceBuilder()
                    .withIPAddress(appiumUrl.getHost())
                    .usingPort(appiumUrl.getPort())
                    .build();
            appiumService.start();
            appiumService.clearOutPutStreams();
            log.info("Appium server started on {}:{}", appiumUrl.getHost(), appiumUrl.getPort());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL", e);
        }
    }

    public static void stopAppium() {
        if (appiumService != null && appiumService.isRunning()) {
            appiumService.stop();
            log.info("Appium server stopped.");
        }
    }

    private static void mockLocationIfEnabled() {
        ConfigReader config = ConfigReader.getInstance();
        if (!Boolean.parseBoolean(config.get("location.mock"))) {
            return;
        }
        double lat = Double.parseDouble(config.get("location.latitude"));
        double lon = Double.parseDouble(config.get("location.longitude"));
        String deviceName = config.get("device.name");
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "adb", "-s", deviceName, "emu", "geo", "fix", String.valueOf(lon), String.valueOf(lat)
            });
            process.waitFor();
            log.info("Device location mocked to ({}, {}) via adb", lat, lon);
        } catch (Exception e) {
            throw new RuntimeException("Failed to mock device location via adb", e);
        }
    }

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
            mockLocationIfEnabled();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium URL: " + config.get("appium.url"), e);
        }
    }

    public static AndroidDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised — call initDriver() first");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

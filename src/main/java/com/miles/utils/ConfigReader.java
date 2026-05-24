package com.miles.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader(){
        properties = new Properties();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        if(instance == null){
            instance = new ConfigReader();
        }
        return instance;
    }

    // System property overrides config file — enables: mvn test -Dtest.email=foo@bar.com
    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Config key not found: " + key);
        }
        return value.trim();
    }


    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}

package com.canyonetcie;

import java.io.*;
import java.util.Properties;

/**
 * Configuration manager for the Permissions Checker application.
 * 
 * Handles loading and saving configuration settings from a properties file.
 */
public class Config {
    
    private static final String CONFIG_FILE = "permissions-checker.properties";
    private final Properties properties;
    
    public Config() {
        this.properties = new Properties();
        loadDefaultSettings();
        loadFromFile();
    }
    
    /**
     * Loads default configuration settings
     */
    private void loadDefaultSettings() {
        properties.setProperty("app.name", "Permissions Checker");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("controlled.folders", "./controlled_folder1,./controlled_folder2,./data");
        properties.setProperty("admin.users", "root,admin,administrator");
        properties.setProperty("verbose.logging", "false");
    }
    
    /**
     * Loads configuration from file if it exists
     */
    private void loadFromFile() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Warning: Could not load configuration file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Saves current configuration to file
     */
    public void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Permissions Checker Configuration");
        } catch (IOException e) {
            System.err.println("Warning: Could not save configuration file: " + e.getMessage());
        }
    }
    
    /**
     * Gets a configuration property
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Gets a configuration property with a default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Sets a configuration property
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Gets the application name
     */
    public String getAppName() {
        return getProperty("app.name");
    }
    
    /**
     * Gets the application version
     */
    public String getAppVersion() {
        return getProperty("app.version");
    }
    
    /**
     * Gets the controlled folders as a comma-separated string
     */
    public String getControlledFolders() {
        return getProperty("controlled.folders");
    }
    
    /**
     * Gets the admin users as a comma-separated string
     */
    public String getAdminUsers() {
        return getProperty("admin.users");
    }
    
    /**
     * Checks if verbose logging is enabled
     */
    public boolean isVerboseLogging() {
        return Boolean.parseBoolean(getProperty("verbose.logging", "false"));
    }
    
    /**
     * Sets verbose logging
     */
    public void setVerboseLogging(boolean verbose) {
        setProperty("verbose.logging", String.valueOf(verbose));
    }
}

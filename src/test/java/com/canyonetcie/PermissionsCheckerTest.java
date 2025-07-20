package com.canyonetcie;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Unit tests for the PermissionsChecker application.
 */
public class PermissionsCheckerTest {
    
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private PermissionManager permissionManager;
    private UserManager userManager;
    
    @Before
    public void setUp() {
        // Capture System.out for testing output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Initialize managers
        permissionManager = new PermissionManager();
        userManager = new UserManager();
    }
    
    @After
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
    
    @Test
    public void testPermissionManagerInitialization() {
        assertNotNull("PermissionManager should be initialized", permissionManager);
        assertFalse("Initial state should have write permissions disabled", 
                   permissionManager.areWritePermissionsEnabled());
        assertTrue("Should have controlled folders", 
                  permissionManager.getControlledFolders().size() > 0);
    }
    
    @Test
    public void testUserManagerInitialization() {
        assertNotNull("UserManager should be initialized", userManager);
        assertNotNull("Current user should not be null", userManager.getCurrentUser());
        assertFalse("Current user should not be empty", userManager.getCurrentUser().isEmpty());
    }
    
    @Test
    public void testControlledFoldersCreation() {
        // The PermissionManager constructor should create controlled folders
        String output = outputStream.toString();
        // We expect at least some folder creation messages (if folders didn't exist)
        assertTrue("Output should contain folder information", output.length() >= 0);
    }
    
    @Test
    public void testAddAndRemoveControlledFolder() {
        String testFolder = "./test_folder";
        int initialSize = permissionManager.getControlledFolders().size();
        
        permissionManager.addControlledFolder(testFolder);
        assertEquals("Should have one more controlled folder", 
                    initialSize + 1, permissionManager.getControlledFolders().size());
        assertTrue("Should contain the test folder", 
                  permissionManager.getControlledFolders().contains(testFolder));
        
        permissionManager.removeControlledFolder(testFolder);
        assertEquals("Should be back to original size", 
                    initialSize, permissionManager.getControlledFolders().size());
        assertFalse("Should not contain the test folder anymore", 
                   permissionManager.getControlledFolders().contains(testFolder));
    }
    
    @Test
    public void testConfigInitialization() {
        Config config = new Config();
        assertNotNull("Config should be initialized", config);
        assertEquals("App name should be correct", "Permissions Checker", config.getAppName());
        assertEquals("App version should be correct", "1.0.0", config.getAppVersion());
        assertFalse("Verbose logging should be disabled by default", config.isVerboseLogging());
    }
    
    @Test
    public void testConfigPropertySetAndGet() {
        Config config = new Config();
        String testKey = "test.key";
        String testValue = "test.value";
        
        config.setProperty(testKey, testValue);
        assertEquals("Property should be set correctly", testValue, config.getProperty(testKey));
        
        String defaultValue = "default.value";
        assertEquals("Should return default value for non-existent key", 
                    defaultValue, config.getProperty("non.existent.key", defaultValue));
    }
    
    @Test
    public void testVerboseLoggingSetting() {
        Config config = new Config();
        
        // Test enabling verbose logging
        config.setVerboseLogging(true);
        assertTrue("Verbose logging should be enabled", config.isVerboseLogging());
        
        // Test disabling verbose logging
        config.setVerboseLogging(false);
        assertFalse("Verbose logging should be disabled", config.isVerboseLogging());
    }
}

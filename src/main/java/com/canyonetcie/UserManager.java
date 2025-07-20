package com.canyonetcie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages user roles and current user information.
 * 
 * This class detects the current system user and determines
 * if they have admin privileges.
 */
public class UserManager {

    private final String currentUser;
    private final boolean hasAdminRole;
    private final Set<String> adminUsers;
    
    public UserManager() {
        // Initialize admin users (you can extend this list)
        adminUsers = new HashSet<>();
        adminUsers.add("root");
        adminUsers.add("admin");
        adminUsers.add("administrator");
        // Add more admin usernames as needed
        
        // Detect current user
        this.currentUser = detectCurrentUser();
        this.hasAdminRole = determineAdminRole();
    }
    
    /**
     * Detects the current system user
     */
    private String detectCurrentUser() {
        // Try to get user from system property first
        String user = System.getProperty("user.name");
        if (user != null && !user.isEmpty()) {
            return user;
        }
        
        // Fallback to environment variable
        user = System.getenv("USER");
        if (user != null && !user.isEmpty()) {
            return user;
        }
        
        // Windows fallback
        user = System.getenv("USERNAME");
        if (user != null && !user.isEmpty()) {
            return user;
        }
        
        return "unknown";
    }
    
    /**
     * Determines if the current user has admin role
     */
    private boolean determineAdminRole() {
        // Check if user is in admin users list
        if (adminUsers.contains(currentUser.toLowerCase())) {
            return true;
        }
        
        // Check if user is running as sudo (Unix/Linux/macOS)
        try {
            String sudoUser = System.getenv("SUDO_USER");
            if (sudoUser != null && !sudoUser.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            // Ignore and continue with other checks
        }
        
        // Check if user has admin privileges by trying to run a privileged command
        try {
            if (isUnixLikeSystem()) {
                return checkUnixAdminPrivileges();
            } else {
                return checkWindowsAdminPrivileges();
            }
        } catch (Exception e) {
            // If we can't determine admin status, assume false for security
            return false;
        }
    }
    
    /**
     * Checks if the current system is Unix-like (Linux, macOS, etc.)
     */
    private boolean isUnixLikeSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("mac") || os.contains("darwin");
    }
    
    /**
     * Checks admin privileges on Unix-like systems
     */
    private boolean checkUnixAdminPrivileges() {
        try {
            // Try to run 'id' command to check if user is in admin groups
            Process process = Runtime.getRuntime().exec("id");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            
            if (line != null) {
                // Check if user is in admin-related groups
                String lowerLine = line.toLowerCase();
                return lowerLine.contains("wheel") || lowerLine.contains("admin") || 
                       lowerLine.contains("sudo") || lowerLine.contains("root");
            }
        } catch (IOException e) {
            // If command fails, assume no admin privileges
        }
        return false;
    }
    
    /**
     * Checks admin privileges on Windows systems
     */
    private boolean checkWindowsAdminPrivileges() {
        try {
            // Try to run 'net session' command which requires admin privileges
            Process process = Runtime.getRuntime().exec("net session");
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the current user name
     */
    public String getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if the current user has admin role
     */
    public boolean hasAdminRole() {
        return hasAdminRole;
    }
    
    /**
     * Adds a username to the admin users list
     */
    public void addAdminUser(String username) {
        adminUsers.add(username.toLowerCase());
    }
    
    /**
     * Removes a username from the admin users list
     */
    public void removeAdminUser(String username) {
        adminUsers.remove(username.toLowerCase());
    }
}


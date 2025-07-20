package com.canyonetcie;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main application class for the Permissions Checker.
 * 
 * This application manages folder permissions based on user roles.
 * It provides a command-line interface with "enable" and "disable" commands
 * to control write permissions for non-admin users.
 */
public class PermissionsChecker {
    
    private final PermissionManager permissionManager;
    private final UserManager userManager;
    private final Scanner scanner;
    
    public PermissionsChecker() {
        this.permissionManager = new PermissionManager();
        this.userManager = new UserManager();
        this.scanner = new Scanner(System.in);
    }
    
    public static void main(String[] args) {
        PermissionsChecker app = new PermissionsChecker();
        
        try {
            app.initialize();
            
            if (args.length > 0) {
                // Command provided as argument
                app.processCommand(args[0]);
            } else {
                // Interactive mode
                app.runInteractiveMode();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } finally {
            app.cleanup();
        }
    }
    
    /**
     * Initialize the application by setting all folders to read-only
     */
    private void initialize() throws IOException {
        System.out.println("Initializing Permissions Checker...");
        System.out.println("Setting all controlled folders to read-only...");
        
        permissionManager.setAllFoldersReadOnly();
        
        System.out.println("Initialization complete.");
        System.out.println("Current user: " + userManager.getCurrentUser());
        System.out.println("Admin privileges: " + userManager.hasAdminRole());
    }
    
    /**
     * Run the application in interactive mode, listening for commands
     */
    private void runInteractiveMode() {
        System.out.println("\nPermissions Checker is now in listening mode.");
        System.out.println("Available commands:");
        System.out.println("  enable  - Enable writing by non-admin users");
        System.out.println("  disable - Disable writing by non-admin users");
        System.out.println("  status  - Show current permission status");
        System.out.println("  help    - Show this help message");
        System.out.println("  exit    - Exit the application");
        System.out.println();
        
        while (true) {
            System.out.print("permissions-checker> ");
            String command = scanner.nextLine().trim().toLowerCase();
            
            if (command.equals("exit")) {
                break;
            }
            
            try {
                processCommand(command);
            } catch (IOException e) {
                System.err.println("Error executing command: " + e.getMessage());
            }
        }
    }
    
    /**
     * Process a single command
     */
    private void processCommand(String command) throws IOException {
        switch (command.toLowerCase()) {
            case "enable":
                enableWritePermissions();
                break;
            case "disable":
                disableWritePermissions();
                break;
            case "status":
                showStatus();
                break;
            case "help":
                showHelp();
                break;
            default:
                System.out.println("Unknown command: " + command);
                System.out.println("Type 'help' for available commands.");
        }
    }
    
    /**
     * Enable write permissions for non-admin users
     */
    private void enableWritePermissions() throws IOException {
        if (!userManager.hasAdminRole()) {
            System.out.println("Error: Admin privileges required to enable write permissions.");
            return;
        }
        
        System.out.println("Enabling write permissions for non-admin users...");
        permissionManager.enableWritePermissions();
        System.out.println("Write permissions enabled successfully.");
    }
    
    /**
     * Disable write permissions for non-admin users
     */
    private void disableWritePermissions() throws IOException {
        if (!userManager.hasAdminRole()) {
            System.out.println("Error: Admin privileges required to disable write permissions.");
            return;
        }
        
        System.out.println("Disabling write permissions for non-admin users...");
        permissionManager.disableWritePermissions();
        System.out.println("Write permissions disabled successfully.");
    }
    
    /**
     * Show current permission status
     */
    private void showStatus() {
        System.out.println("Current Status:");
        System.out.println("  User: " + userManager.getCurrentUser());
        System.out.println("  Admin Role: " + userManager.hasAdminRole());
        System.out.println("  Write Permissions Enabled: " + permissionManager.areWritePermissionsEnabled());
        System.out.println("  Controlled Folders: " + permissionManager.getControlledFolders().size());
    }
    
    /**
     * Show help information
     */
    private void showHelp() {
        System.out.println("\nPermissions Checker Help:");
        System.out.println("  enable  - Enable writing by non-admin users (admin only)");
        System.out.println("  disable - Disable writing by non-admin users (admin only)");
        System.out.println("  status  - Show current permission status");
        System.out.println("  help    - Show this help message");
        System.out.println("  exit    - Exit the application");
        System.out.println();
    }
    
    /**
     * Cleanup resources
     */
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
}

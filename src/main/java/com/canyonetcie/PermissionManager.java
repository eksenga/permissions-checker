package com.canyonetcie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

/**
 * Manages folder permissions based on user roles.
 * 
 * This class handles the actual file system permission changes
 * for controlled folders.
 */
public class PermissionManager {

    private final Map<String, Boolean> controlledFolders = new HashMap<>();
    private final Set<String> defaultFolders;
    
    public PermissionManager() {
        // Initialize with default controlled folders
        defaultFolders = new HashSet<>();
        defaultFolders.add("./controlled_folder1");
        defaultFolders.add("./controlled_folder2");
        defaultFolders.add("./data");
        
        // Create folders if they don't exist and initialize permission tracking
        for (String folder : defaultFolders) {
            createFolderIfNotExists(folder);
            controlledFolders.put(folder, false); // Initially read-only
        }
    }
    
    /**
     * Creates a folder if it doesn't exist
     */
    private void createFolderIfNotExists(String folderPath) {
        try {
            Path path = Paths.get(folderPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created controlled folder: " + folderPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create folder: " + folderPath + " - " + e.getMessage());
        }
    }

    /**
     * Sets all controlled folders to read-only
     */
    public void setAllFoldersReadOnly() throws IOException {
        for (String folder : controlledFolders.keySet()) {
            setFolderReadOnly(folder, true);
            controlledFolders.put(folder, false);
        }
    }

    /**
     * Enables write permissions for non-admin users on all controlled folders
     */
    public void enableWritePermissions() throws IOException {
        for (String folder : controlledFolders.keySet()) {
            setFolderReadOnly(folder, false);
            controlledFolders.put(folder, true);
        }
    }

    /**
     * Disables write permissions for non-admin users on all controlled folders
     */
    public void disableWritePermissions() throws IOException {
        for (String folder : controlledFolders.keySet()) {
            setFolderReadOnly(folder, true);
            controlledFolders.put(folder, false);
        }
    }
    
    /**
     * Sets the read-only status of a folder
     */
    private void setFolderReadOnly(String folderPath, boolean readOnly) throws IOException {
        Path path = Paths.get(folderPath);
        
        if (!Files.exists(path)) {
            System.out.println("Warning: Folder does not exist: " + folderPath);
            return;
        }
        
        try {
            // For Unix-like systems (including macOS)
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Windows approach
                File file = path.toFile();
                file.setWritable(!readOnly);
            } else {
                // Unix/Linux/macOS approach using POSIX permissions
                Set<PosixFilePermission> permissions;
                
                if (readOnly) {
                    // Read and execute only (r-xr-xr-x)
                    permissions = PosixFilePermissions.fromString("r-xr-xr-x");
                } else {
                    // Read, write, and execute (rwxrwxrwx)
                    permissions = PosixFilePermissions.fromString("rwxrwxrwx");
                }
                
                Files.setPosixFilePermissions(path, permissions);
            }
            
            System.out.println("Set " + folderPath + " to " + (readOnly ? "read-only" : "read-write"));
            
        } catch (Exception e) {
            System.err.println("Failed to set permissions for " + folderPath + ": " + e.getMessage());
            throw new IOException("Permission change failed", e);
        }
    }

    /**
     * Checks if write permissions are enabled for all controlled folders
     */
    public boolean areWritePermissionsEnabled() {
        return controlledFolders.values().stream().allMatch(Boolean::booleanValue);
    }

    /**
     * Gets the set of controlled folder paths
     */
    public Set<String> getControlledFolders() {
        return new HashSet<>(controlledFolders.keySet());
    }
    
    /**
     * Adds a new folder to be controlled by this manager
     */
    public void addControlledFolder(String folderPath) {
        createFolderIfNotExists(folderPath);
        controlledFolders.put(folderPath, false); // Initially read-only
    }
    
    /**
     * Removes a folder from being controlled by this manager
     */
    public void removeControlledFolder(String folderPath) {
        controlledFolders.remove(folderPath);
    }
}


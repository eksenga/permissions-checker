# Java Permissions Checker

A Java implementation of the permissions checker application that manages folder permissions based on user roles.

## Overview

This application provides a command-line interface for controlling write permissions on specific folders. It restricts non-admin users from modifying files in controlled directories while allowing admin users to enable/disable these restrictions.

## Features

- **Role-based Access Control**: Differentiates between admin and non-admin users
- **Command-line Interface**: Interactive mode and single-command execution
- **Cross-platform Support**: Works on Windows, macOS, and Linux
- **Configurable**: Settings can be customized via properties file
- **Real File System Integration**: Actually modifies folder permissions

## Project Structure

```
src/
├── main/java/com/canyonetcie/
│   ├── PermissionsChecker.java     # Main application class
│   ├── PermissionManager.java      # Handles folder permission operations
│   ├── UserManager.java           # Manages user roles and detection
│   └── Config.java                # Configuration management
└── test/java/com/canyonetcie/
    └── PermissionsCheckerTest.java # Unit tests
```

## Requirements

- Java 11 or higher
- Gradle 8.5 or higher (or use the included Gradle wrapper)
- Appropriate system permissions to modify folder permissions

## Building the Application

```bash
# Compile the application
./gradlew build

# Run tests only
./gradlew test

# Create executable JAR
./gradlew jar

# Create fat JAR with all dependencies
./gradlew fatJar
```

## Running the Application

### Interactive Mode

```bash
# Using Gradle
./gradlew runApp

# Or using the JAR
java -jar build/libs/permissions-checker-1.0.0.jar
```

### Single Command Mode

```bash
# Using Gradle tasks
./gradlew runWithCommand -Pcommand=enable
./gradlew runWithCommand -Pcommand=disable
./gradlew runWithCommand -Pcommand=status

# Or using the JAR
java -jar build/libs/permissions-checker-1.0.0.jar enable
java -jar build/libs/permissions-checker-1.0.0.jar disable
java -jar build/libs/permissions-checker-1.0.0.jar status
```

## Available Commands

- **`enable`** - Enable writing by non-admin users (requires admin privileges)
- **`disable`** - Disable writing by non-admin users (requires admin privileges)
- **`status`** - Show current permission status and user information
- **`help`** - Display help information
- **`exit`** - Exit the application (interactive mode only)

## Configuration

The application uses a `permissions-checker.properties` file for configuration:

```properties
app.name=Permissions Checker
app.version=1.0.0
controlled.folders=./controlled_folder1,./controlled_folder2,./data
admin.users=root,admin,administrator
verbose.logging=false
```

### Configuration Options

- **`controlled.folders`**: Comma-separated list of folders to control
- **`admin.users`**: Comma-separated list of usernames with admin privileges
- **`verbose.logging`**: Enable/disable verbose logging

## Admin User Detection

The application determines admin privileges through multiple methods:

1. **Username matching**: Checks if current user is in admin users list
2. **Sudo detection**: Checks for SUDO_USER environment variable
3. **System groups**: On Unix-like systems, checks if user is in admin groups (wheel, admin, sudo)
4. **Windows privileges**: On Windows, attempts to run privileged commands

## Controlled Folders

By default, the application creates and controls these folders:
- `./controlled_folder1`
- `./controlled_folder2`  
- `./data`

These folders are initially set to read-only when the application starts.

## Permission Changes

### Unix/Linux/macOS
- **Read-only**: `r-xr-xr-x` (555)
- **Read-write**: `rwxrwxrwx` (777)

### Windows
- Uses `File.setWritable()` method

## Error Handling

The application handles various error conditions:
- Missing folders (creates them automatically)
- Permission change failures
- Invalid commands
- Insufficient privileges

## Security Considerations

- Non-admin users cannot modify permissions
- Admin privilege detection follows security best practices
- Graceful degradation when permission operations fail
- No plain-text storage of sensitive information

## Development

### Adding New Controlled Folders

```java
PermissionManager pm = new PermissionManager();
pm.addControlledFolder("/path/to/new/folder");
```

### Adding New Admin Users

```java
UserManager um = new UserManager();
um.addAdminUser("newadmin");
```

### Running Tests

```bash
./gradlew test
```

## Troubleshooting

### Common Issues

1. **"Permission denied" errors**: Run with appropriate privileges or as admin
2. **Folder not found**: Application will create missing folders automatically
3. **Admin detection fails**: Add your username to the admin users list in config

### Debug Mode

Enable verbose logging in the configuration file:
```properties
verbose.logging=true
```

## License

This project is part of the permissions checker suite and follows the same licensing as the main project.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## Future Enhancements

- [ ] GUI interface
- [ ] Database integration for user management
- [ ] Audit logging
- [ ] Fine-grained permission control
- [ ] Integration with system authentication

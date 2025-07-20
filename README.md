This document outlines the requirements and use cases for the permissions checker application.

The application will have several versions. A java version, Golang version and python version to start with.

Summary.

The application that limits certain users from modifying or updating files in specific directories of the application.
This limitation is defined by a lack of user permission which is defined by roles assigned to a user account. For example, users with the admin role should be able to create, modify or delete files in any folder that the application controles. However, users without the admin persmission should not be able to do it.
The application will have a simple command line interface. When first run it will update all folder permissions to read-only. It will then sit in a listening mode waiting for commands (or can be invoked with a the command as a parameter). The commands are as follows:
1. "enable": Updates the folder permissions to enable writing by non admin users.
2. "disable": Updates the folder permissions to disable writing by non admin users.  

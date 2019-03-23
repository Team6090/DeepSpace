# FRC6090-2019
The official repository of Team 6090's 2019 Deep Space code.

## Versions Table
These tables store the latest versions of the software we use, and which of our
devices are updated, and which ones need to be updated.

| Vendor Libraries | Latest Known Version | Updated Laptops | Laptops to Update |
|------------------|----------------------|-----------------|-------------------|
| WPILib           | 2019.4.1             | 1, 2, 3         |                   |
| NavX             | 3.1.366              | 1, 2, 3         |                   |
| CTRE Phoenix     | 5.14.1               | 1, 2, 3         |                   |
| RevRobotics      | 1.1.9                | 1, 2, 3         |                   |

| Software         | Latest Known Version | Updated Laptops | Laptops to Update |
|------------------|----------------------|-----------------|-------------------|
| OpenJDK          | 11.0.2               | 1, 2, 3         |                   |
| Gradle           | 5.1                  | 1, 2, 3         |                   |
| FRC Update Suite | 2019.2.0             | 1, 2, 3         |                   |
| VS Code          | 1.32.3               |                 | 1, 2, 3           |
| Git              | 2.17.1+              | 1, 2, 3         |                   |
| Phoenix Tuner    | 1.4.0                | 1, 2, 3         |                   |
| Spark Max Client | 1.0.0                | 2, 3            | 1                 |
| Etcher           | 1.4.8                | 2, 3            | 1                 |

| Firmware  | Latest known Version | Updated Devices       | Devices to Update     |
|-----------|----------------------|-----------------------|-----------------------|
| Spark Max | 1.1.33               | Practice, Competition |                       |
| RoboRIO   | 6.0.0                | Practice, Competition |                       |

| Images    | Latest Known Version | Updated Devices       | Devices to Update     |
|-----------|----------------------|-----------------------|-----------------------|
| RoboRIO   | 2019.14              | Practice, Competition |                       |
| LimeLight | 2019.6               | Practice              | Competition           |


## Contributors
### Students
- Jordan Bancino
- Ethan Snyder
- Collin Heavner

### Mentors
- Rod K
- Joe S

## Building & Deploying
### Common Issues
- Gradle may fail to resolve some dependencies. If this happens, you'll need to run this command to manually download
them: 

        ./gradlew downloadAll

- Gradle may not be able to find Java. Make sure you add `java` to the system path, and set the `JAVA_HOME` environment variable. You may have to restart your IDE after making these changes. **DO NOT** modify the Gradle scripts or any of the code to point to Java.

## Tools
TODO: Document the `/tools` directory here.

## Linux
Linux users do not need the Update Suite to deploy to a RoboRIO via ethernet or USB, but rather, can use Jordan Bancino's [rio-network.sh](tools/rio-network.sh) to set up the DHCP server to properly connect. This script works over both USB and ethernet.

## Contributing
TODO: add contributing guide here.

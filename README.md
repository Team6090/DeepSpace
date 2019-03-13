# FRC6090-2019
The official repository of Team 6090's 2019 Deep Space code.

## Versions Table

### Key
- P# - A "P" placed in from of a number/device refers to a device on the practice bot.
- C# - A "C" placed in from of a number/device refers to a device on the competition bot.

### Data
| Vendor Libraries | Latest Known Version  | Updated Laptops | Laptops to Update |
|------------------|-----------------------|-----------------|-------------------|
| WPILib           | 2019.4.1              | 1, 2, 3         |                   |
| NavX             | 3.1.366               | 1, 2, 3         |                   |
| CTRE Phoenix     | 5.14.1                | 1, 2, 3         |                   |
| RevRobotics      | 1.1.9                 | 1, 2, 3         |                   |

| Software         | Latest Known Version | Updated Laptops | Laptops to Update |
|------------------|----------------------|-----------------|-------------------|
| OpenJDK          | 11.0.2               | 1, 2, 3         |                   |
| Gradle           | 5.1                  | 1, 2, 3         |                   |
| FRC Update Suite | 2019.14              | 1, 2, 3         |                   |

| Firmware  | Latest known Version | Updated Devices (CAN ID, etc.) | Devices to Update    |
|-----------|----------------------|--------------------------------|----------------------|
| Spark Max | 1.1.33               | Practice, Competition          |                      |
| RoboRIO   | 6.0.0                | Practice, Competition          |                      |

| Images    | Latest Known Version | Updated Devices       | Devices to Update    |
|-----------|----------------------|-----------------------|----------------------|
| RoboRIO   | 2019.14              | Practice, Competition |                      |
| LimeLight | 2019.5               | Practice, Competition |                      |


## Contributors
### Students
- Jordan Bancino
- Ethan Snyder
- Collin Heavner

### Mentors
- Rod K
- Joe S

## Building & Deploying
To build the current code base, you'll need the base tools:

- JDK 11.x.x (Currently, the latest release is 11.0.1 for the RoboRIO)
- Gradle 5.x.x (Currently WPILib officially supports 5.0)

Make sure Java is in the system path, or set `JAVA_HOME`. Then, in the root of the project, run this command:

```
./gradlew build
 ```
 
This will ensure all dependencies are resolved, and a build can be completed.
It will be obvious whether or not there were errors. 

---

### Common Issues
- Gradle may fail to resolve some dependencies. If this happens, you'll need to run this command to manually download
them: 

```
./gradlew downloadAll
```
---

To deploy the code to a RoboRIO, ensure that it is powered on, and connected to either the FRC-issued radio (via ethernet)
or a PC (via ethernet or USB). If connecting via USB, make sure the [FRC Update Suite](https://wpilib.screenstepslive.com/s/currentCS/m/cpp/l/1027499-installing-the-frc-update-suite-all-languages) is installed, because it contains the USB driver for the RoboRIO. Deploying via Ethernet connected to a PC will also require the Update Suite. Linux users do not need the Update Suite to deploy via ethernet or USB, but rather, can use Jordan Bancino's [rio-network.sh]() to set up the DHCP server to properly connect. This script works over both USB and ethernet.

Once a connection is properly established, run this command:

```
./gradlew deploy
```

This will deploy the code to the RoboRIO. You can the run the FRC Driver Station (included in the FRC Update Suite) to run it.

## Contributing
TODO: add contributing guide here.

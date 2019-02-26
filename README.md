# FRC6090-2019
The official repository of Team 6090's 2019 Deep Space code.

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

    ./gradlew build
    
This will ensure all dependencies are resolved, and a build can be completed.
It will be obvious whether or not there were errors. 

---

### Common Issues
- Gradle may fail to resolve some dependencies. If this happens, you'll need to run this command to manually download
them: 

    `./gradlew downloadAll`
    
---

To deploy the code to a RoboRIO, ensure that it is powered on, and connected to either the FRC-issued radio (via ethernet)
or a PC (via ethernet or USB).

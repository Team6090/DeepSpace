/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An interface to easily control multiple cameras, and switching between them.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class CameraController {
  
  /* The array index corresponds to the camera ID. */
  private UsbCamera[] usbCamera;

  /* The camera server, used to set sources. */
  private CameraServer cameraServer = CameraServer.getInstance();

  /* 
   * This is to keep track of what the currently set camera is. This ensures that the
   * code does not reset the camera server source over and over again, but only
   * when the desired source changes.
   */
  private int currentCamera;

  /* The default camera. */
  private int defaultCamera;

  /* The key to use in the network tables. */
  private String smartDashboardKey;

  /**
   * Create the USB cameras.
   * @param numberOfCameras How many cameras to set up.
   * @param cameraFps The frames-per-second to set each camera at.
   * @param cameraWidth The width resolution to set each camera at.
   * @param cameraHeight The height resolution to set each camera at.
   * @param defaultCamera The default camera to initialize with.
   * @param smartDashboardKey The key to monitor in the network tables.
   */
  public CameraController(int numberOfCameras, int cameraFps, int cameraWidth, int cameraHeight, int defaultCamera, String smartDashboardKey) {
    /* Create the array of cameras */
    usbCamera = new UsbCamera[numberOfCameras];
    /* Populate the array, and set the settings on each camera. */
    for (int i = 0; i < usbCamera.length; i++) {
      usbCamera[i] = cameraServer.startAutomaticCapture(i);
      usbCamera[i].setResolution(cameraWidth, cameraHeight);
      usbCamera[i].setFPS(cameraFps);
    }
    /* Set the default camera as the initial source. */
    SmartDashboard.putNumber(smartDashboardKey, defaultCamera);

    this.defaultCamera = defaultCamera;
    this.smartDashboardKey = smartDashboardKey;
  }

  /**
   * Update the CameraServer feed to stream from the camera whose ID is
   * specified in the SmartDashboard. Call this function in {@code Robot.robotPeriodic()}.
   */
  public void update() {
    /* Camera switching functionality on the joystick. */
    switch (Robot.oi.getJoystickPOV()) {
      case UP:
        Robot.limelight.setStreamingMode(Limelight.StreamMode.PIP_MAIN);
        break;
      case DOWN:
        Robot.limelight.setStreamingMode(Limelight.StreamMode.PIP_SECONDARY);
        break;
      case LEFT:
        SmartDashboard.putNumber(smartDashboardKey, RobotMap.frontCamera);
        break;
      case RIGHT:
        SmartDashboard.putNumber(smartDashboardKey, RobotMap.rearCamera);
        break;
      default:
        break;
    }

    /*
     * Fetch the desired camera ID from the Smart Dashboard.
     */ 
    int setCamera = (int) SmartDashboard.getNumber(smartDashboardKey, defaultCamera);

    /*
     * If the desired camera is within bounds, and is not the currently set
     * camera, change the camera source.
     */
    if (((setCamera >= 0) && (setCamera < usbCamera.length)) && (setCamera != currentCamera)) {
      cameraServer.getServer().setSource(usbCamera[setCamera]);
      currentCamera = setCamera;
    }
  }
}

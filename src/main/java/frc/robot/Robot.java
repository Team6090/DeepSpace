/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.lib.RobotDebug;
import frc.robot.lib.vision.Limelight;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  /* Subsystems */
  public static DriveTrain drivetrain = new DriveTrain();
  public static Elevator elevator = new Elevator();
  /* Construct intake with the compressor either enabled or disabled. */
  public static Intake intake = new Intake(false);
  //TODO: set back to true

  /* Limelight related functionality */
  public static Limelight limelight = new Limelight();

  /* Joystick related functionality. */
  public static OI oi = new OI();

  /*
   * The RobotDebug class outputs just about every value we can possibly
   * read to the smart dashboard, but only if it is constructed to true.
   * Setting to false will disable all output, thus allowing for quickly
   * toggling this functionality. 
   */
  private RobotDebug robotDebug = new RobotDebug(true);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    /* Limelight defaults */
    limelight.setLedMode(Limelight.LedState.FORCE_OFF);
    limelight.setCameraMode(Limelight.CameraMode.VISION);
    limelight.setStreamingMode(Limelight.StreamMode.PIP_SECONDARY);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    /*
     * Update the SmartDashboard data, when enabled. 
     */
    robotDebug.update();

    /*
     * Update the Limelight's settings based on external factors.
     */
    limelight.update();

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    /*
     * This makes sure that the autonomous stops running when
     * teleop starts running. If you want the autonomous to
     * continue until interrupted by another command, remove
     * this line or comment it out.
     */
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

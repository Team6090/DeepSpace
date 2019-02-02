/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.DriveWithJoystick;

/**
 * The drivetrain is a simple tank drive, with two motors on each side.
 */
public class DriveTrain extends Subsystem {

  /* Set up the motors. */
  private final CANSparkMax leftMotor = new CANSparkMax(RobotMap.leftMotor, MotorType.kBrushless);
  private final CANSparkMax leftSlaveMotor = new CANSparkMax(RobotMap.leftSlaveMotor, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(RobotMap.rightMotor, MotorType.kBrushless);
  private final CANSparkMax rightSlaveMotor = new CANSparkMax(RobotMap.rightSlaveMotor, MotorType.kBrushless);

  /* Speed controller groups. */
  private final SpeedControllerGroup leftSideMotors = new SpeedControllerGroup(leftMotor, leftSlaveMotor);
  private final SpeedControllerGroup rightSideMotors = new SpeedControllerGroup(rightMotor, rightSlaveMotor);

  /* The differential drive. */
  private final DifferentialDrive diffdrive = new DifferentialDrive(leftSideMotors, rightSideMotors);

  /* Create the AHRS NavX Gyro */
  private final AHRS navxGyro = new AHRS(SPI.Port.kMXP);

  /**
   * On init, set a small deadband on the differential drive.
   */
  public DriveTrain() {
    diffdrive.setDeadband(.02);
  }

  /**
   * By default, enable control from the joystick.
   */
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }

  /**
   * Used for driving with the joystick
   * @param y The Y value from the joystick.
   * @param z The Z value from the joystick.
   */
  public void arcadeDrive(double y, double z) {
    diffdrive.arcadeDrive(y, z);
  }

  /**
   * Drive the left motors.
   */
  public void setLeft(double speed) {
    leftSideMotors.set(speed);
  }

  /**
   * Drive the right side motors.
   */
  public void setRight(double speed) {
    rightSideMotors.set(speed);
  }

  /**
   * Set the speed of both the left and right motors.
   * @param speedLeft The desired left motor(s) speed percentage.
   * @param speedRight The desired right motor(s) speed percentange.
   */
  public void set(double speedLeft, double speedRight) {
    setLeft(speedLeft);
    setRight(speedRight);
  }

  /**
   * Get the left side motors speed percentage.
   */
  public double getLeft() {
    return leftSideMotors.get();
  }

  /**
   * Get the right side motors speed percentage.
   */
  public double getRight() {
    return rightSideMotors.get();
  }

  /**
   * The right motor position
   * @return The absolute encoder value of the right motor.
   */
  public double getRightEncoderPosition() {
    /* Get Right drive motor Position */
    return rightMotor.getEncoder().getPosition();
  }

  /**
   * The left motor position
   * @return The absolute encoder value of the left motor.
   */
  public double getLeftEncoderPosition() {
    /* Get Right drive motor Position */
    return leftMotor.getEncoder().getPosition();
  }

  /**
   * Stop all motors.
   */
  public void stop() {
    set(0.0, 0.0);
  }

  /**
   * Get the yaw from the NavX Gyro.
   * @return Gyro yaw.
   */
  public final float getGyroYaw() {
    /* TODO: Need to add drift correction to this code */
    return navxGyro.getYaw();
  }

  /**
   * Get the pitch from the NavX Gyro.
   * @return Gyro pitch.
   */
  public final float getGyroPitch() {
    return navxGyro.getPitch();
  }

  /**
   * Get the compass heading from the NavX Gyro.
   * @return Gyro Compass Heading.
   */
  public final float getGyroCompassHeading() {
    return navxGyro.getCompassHeading();
  }

  /**
   * Get the roll from the NavX Gyro.
   * @return Gyro roll.
   */
  public final float getGyroRoll(){     
    return navxGyro.getRoll();
  }

  /**
   * Zero the Yaw on the NavX Gyro.
   */
  public final void zeroGyroYaw() {
    navxGyro.zeroYaw();
  }

}

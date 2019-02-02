/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;

public class DriveTrain extends Subsystem {

  private final CANSparkMax leftMotor = new CANSparkMax(RobotMap.leftMotor, MotorType.kBrushless);
  private final CANSparkMax leftSlaveMotor = new CANSparkMax(RobotMap.leftSlaveMotor, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(RobotMap.rightMotor, MotorType.kBrushless);
  private final CANSparkMax rightSlaveMotor = new CANSparkMax(RobotMap.rightSlaveMotor, MotorType.kBrushless);

  private final SpeedControllerGroup leftSideMotors = new SpeedControllerGroup(leftMotor, leftSlaveMotor);
  private final SpeedControllerGroup rightSideMotors = new SpeedControllerGroup(rightMotor, rightSlaveMotor);

  private final DifferentialDrive diffdrive = new DifferentialDrive(leftSideMotors, rightSideMotors);

  public DriveTrain() {
    diffdrive.setDeadband(.02);

  }

  @Override
  public void initDefaultCommand() {
    
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
}

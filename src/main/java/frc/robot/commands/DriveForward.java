/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Drive the drivetrain forward for a set amount of encoder counts, or time,
 * whichever comes first.
 * @author Collin Heavner, Christian Mattingly
 * @version 1.0
 * @since 1.0
 */
public class DriveForward extends Command {
  double baseEncoderCount, thresholdEncoderCount, currentEncoderCount = 0;
  double encoderCount;
  boolean haveBaseCount;
  long baseTime, thresholdTime, durationUntilTimeOut;
  double driveForwardSpeedRef;
  float startingAngle;
  float driftCorrectionTargetMax;
  float driftCorrectionTargetMin;
  float correctionDeadband = 0.2f;
  double speedRefRight;
  double speedRefLeft;
  double speedRefAdjLeft;
  float currentAngle;
  double speedRefLeftFinal;

  /**
   * Drive forward.
   * @param encoderCount The number of encoder counts to drive forward. For the SparkMax motor controllers, this
   * is actually motor revolutions.
   * @param durationUntilTimeOut The duration to drive forward. Driving will stop when either the motor revolution
   * count is at the target, or this duration is reached.
   * @param driveForwardSpeedRef The speed at which to drive forward.
   */
  public DriveForward(double encoderCount, long durationUntilTimeOut, double driveForwardSpeedRef) {
    this.encoderCount = encoderCount;
    this.durationUntilTimeOut = durationUntilTimeOut;
    this.driveForwardSpeedRef = driveForwardSpeedRef;
    this.speedRefRight = driveForwardSpeedRef;
    this.speedRefLeft = driveForwardSpeedRef;
    this.speedRefAdjLeft = 0.0d;
    haveBaseCount = false;
    requires(Robot.drivetrain);
  }

  /**
   * Calculate the threshhold time, get the current gyro angle, and calculate drift bounds.
   */
  @Override
  protected void initialize() {
    /* Start clock and angle value readings*/
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + durationUntilTimeOut;
    startingAngle = currentAngle = Robot.drivetrain.getGyroYaw();
    driftCorrectionTargetMax = startingAngle + correctionDeadband;
    driftCorrectionTargetMin = startingAngle - correctionDeadband;
  }

  /**
   * Drive the robot forward. If it starts to drift, correct.
   */
  @Override
  protected void execute() {
    if (!haveBaseCount) {
      baseEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
      thresholdEncoderCount = baseEncoderCount + encoderCount;
      haveBaseCount = true;
    }

    currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
    currentAngle = Robot.drivetrain.getGyroYaw();

    /* Sync robot to gyro angle setpoint */
    speedRefAdjLeft = Robot.drivetrain.syncAngle(startingAngle, currentAngle);
    speedRefLeftFinal = speedRefLeft + speedRefAdjLeft;
    /* invert right side motor to drive forward  */
    Robot.drivetrain.set(speedRefLeftFinal, -speedRefRight);
  }
  
  /**
   * Stop when either the timeout is reached, or the encoder count is reached.
   */
  @Override
  protected boolean isFinished(){
    if (((thresholdEncoderCount) <= currentEncoderCount) || ((System.currentTimeMillis()) >= thresholdTime)){
      haveBaseCount = false;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Stop the drivetrain.
   */
  @Override
  protected void end(){
    Robot.drivetrain.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }
}

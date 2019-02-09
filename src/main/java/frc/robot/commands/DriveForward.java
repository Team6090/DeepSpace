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
 * @author Collin Heavner
 * @version 1.0
 * @since 1.0
 */

public class DriveForward extends Command {
  double baseEncoderCount, thresholdEncoderCount, currentEncoderPosition = 0;
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
  double currentAngle;
  double speedRefLeftFinal;

  public DriveForward(int encoderCount, long durationUntilTimeOut, double driveForwardSpeedRef) {
    this.encoderCount = encoderCount;
    this.durationUntilTimeOut = durationUntilTimeOut;
    this.driveForwardSpeedRef = driveForwardSpeedRef;
    this.speedRefRight = driveForwardSpeedRef;
    this.speedRefLeft = driveForwardSpeedRef;
    this.speedRefAdjLeft = 0.0d;
    haveBaseCount = false;
    requires(Robot.drivetrain);
    
  }
  @Override
  protected void initialize() {
    /* Start clock and angle value readings*/
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + durationUntilTimeOut;
    startingAngle = Robot.drivetrain.getGyroYaw();
    currentAngle = Robot.drivetrain.getGyroYaw();
    driftCorrectionTargetMax = startingAngle + correctionDeadband;
    driftCorrectionTargetMin = startingAngle - correctionDeadband;
  }
  @Override
  protected void execute() {
    if (!haveBaseCount) {
      baseEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
      thresholdEncoderCount = baseEncoderCount + encoderCount;
      haveBaseCount = true;
    }

      currentEncoderPosition = Robot.drivetrain.getLeftEncoderPosition();
      currentAngle = Robot.drivetrain.getGyroYaw();
      speedRefAdjLeft = Robot.drivetrain.syncAngle(startingAngle, currentAngle); 		// Sync robot to gyro angle setpoint
      speedRefLeftFinal = speedRefLeft + speedRefAdjLeft;
      System.out.println ("speedRefAdjLeft " + speedRefAdjLeft); 
      Robot.drivetrain.set (speedRefLeftFinal, -speedRefRight); //invert right side motor to drive forward 
  }
  

  @Override
  protected boolean isFinished(){
    if (((baseEncoderCount + currentEncoderPosition) >= thresholdEncoderCount) || ((System.currentTimeMillis()) >= thresholdTime)){
      haveBaseCount = false;
      return true;
    } else {
      System.out.println (" Current Angle = " + currentAngle);
      return false;
    }
  }

  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import frc.robot.lib.vision.Limelight;
import frc.robot.lib.vision.LimelightCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.Robot;

public class GaffTapeAlign extends LimelightCommand {
  double motorRevs = DriveTrain.distanceToMotorRevs2(36.0d); 
  boolean hasTarget = Robot.limelight.hasValidTargets();
  boolean endProgram, CW;
  double speedLeft, speedRight, speedRef, speedMultiplier;
  double horizontalOffset, horizontalOffsetLowerBound = -5.0d, horizontalOffsetUpperBound = 5.0d;
  double currentEncoderCount, baseEncoderCountRight, baseEncoderCountLeft, thresholdEncoderCount, previousEncoderCount, encoderCountDifference;
  double baseTime, thresholdTime, duration;

  public GaffTapeAlign(double speedRef, double speedMultiplier, double xOffsetLowerBound, double xOffsetUpperBound, double encoderCountDifference, double duration) {
    super(Limelight.GAFF_PIPELINE);
    this.speedMultiplier = speedMultiplier;
    this.horizontalOffsetLowerBound = xOffsetLowerBound;
    this.horizontalOffsetUpperBound = xOffsetUpperBound;
    this.speedRef = speedRef;
    this.encoderCountDifference = encoderCountDifference;
    this.duration = duration;
    requires(Robot.drivetrain);
  }

  /* Called just before this Command runs the first time */
  @Override
  protected void initialize() {
    super.initialize();
    /* Initialize timer */
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;
    /* Extra precautions, if the pipeline wasn't set correctly. Exits the command */
    if (Robot.limelight.getPipe() != 1) {
      endProgram = true;
      System.out.println("Invalid pipeline, command stopping.");
    }

    baseEncoderCountLeft = Robot.drivetrain.getLeftEncoderPosition();
    baseEncoderCountRight = Robot.drivetrain.getRightEncoderPosition();

    /* Sets motor speeds based on the speedRef */
    speedRight = -speedRef;
    speedLeft = speedRef;
    /* Figures out if there's a target or not, calculates offset, calculates threshold encoder */
    hasTarget = Robot.limelight.hasValidTargets();
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    thresholdEncoderCount = baseEncoderCountLeft + motorRevs;
    /* Printing out init values to SmartDashboard */
    Robot.debug.put("thresholdEncoderCount", thresholdEncoderCount);
    Robot.debug.put("baseEncoderCountLeft", baseEncoderCountLeft);
    Robot.debug.put("baseEncoderCountRight", baseEncoderCountRight);
  }

  /* Called repeatedly when this Command is scheduled to run */
  @Override
  protected void execute() {
  
    /* This will diagnose what's needed to correct. Will only run if there is a > 2 degree offset */
    if (hasTarget) {
      horizontalOffset = Robot.limelight.getHorizontalOffset();
      if (horizontalOffset < 0.0d) {
        if (horizontalOffset > horizontalOffsetLowerBound && horizontalOffset < horizontalOffsetUpperBound) {
          Robot.drivetrain.set(speedLeft, speedRight);
          currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
        }
        else {
          Robot.drivetrain.set(speedLeft, (speedRight * speedMultiplier));
          currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
        }
      }
      else if (horizontalOffset > 0.0d) {
        if (horizontalOffset > horizontalOffsetLowerBound && horizontalOffset < horizontalOffsetUpperBound) {
          Robot.drivetrain.set(speedLeft, speedRight);
          currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
        }
        else {
          Robot.drivetrain.set((speedLeft * speedMultiplier), speedRight);
          if (Robot.drivetrain.getRightEncoderPosition() < 0.0d) {
          currentEncoderCount = -Robot.drivetrain.getRightEncoderPosition();
          }
          else {
            currentEncoderCount = Robot.drivetrain.getRightEncoderPosition();
          }
        }
      }
      else {
        Robot.drivetrain.set(speedLeft, speedRight);
        currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
      }
    }
    /* Controls for the stopping when there was a collision */
    if (Robot.drivetrain.getLeft() > 0) {
      if ((currentEncoderCount - previousEncoderCount) < encoderCountDifference) {
        Robot.drivetrain.set(0.0d, 0.0d);
      }
    }
    previousEncoderCount = currentEncoderCount;
  }

  /* Make this return true when this Command no longer needs to run execute() */
  @Override
  protected boolean isFinished() {
    
    /* Uncomment to print things out to SmartDashboard */
    //Robot.debug.put("motorRevs", motorRevs);
    //Robot.debug.put("motorRevscalc", (currentEncoderCount - baseEncoderCountLeft));
    //Robot.debug.put("currentEncoderCount", currentEncoderCount);

    return (endProgram || System.currentTimeMillis() > thresholdTime || (currentEncoderCount >= thresholdEncoderCount));
  }

  /* Called once after isFinished returns true */
  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  /* Called when another command which requires one or more of the same subsystems is scheduled to run */
  @Override
  protected void interrupted() {
  }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.LimelightCommand;
import frc.robot.Robot;

/**
 * Use the LimeLight to follow a vision target.
 * @author Ethan Snyder
 * @author Collin Heavner
 * @version 1.0
 * @since 1.0
 */
public class PuppyDog extends LimelightCommand {

  boolean haveTarget;
  double area, horizontalOffset, targetArea;
  double speedY, speedZ;
  double baseTime, thresholdTime, duration;
  double startingAngle, currentAngle, upperBoundAngle, lowerBoundAngle;
  boolean CWDone = false;
  boolean CCWDone = false;
  boolean targetAcquired;

  /**
   * Set up PuppyDog.
   * @param speedY Forward/backward speed.
   * @param speedZ Rotation speed.
   * @param targetArea The target area to stop at.
   * @param duration How long to run the command for.
   */
  public PuppyDog(double speedY, double speedZ, double targetArea, double duration) {
    this.speedY = speedY;
    this.speedZ = speedZ;
    this.targetArea = targetArea;
    this.duration = duration;
    requires(Robot.drivetrain);
  }

  /**
   * Start the clock, convert the gyro angle if needed.
   */
  @Override
  protected void initialize() {
    super.initialize();

    /*
     * Sets the timeout (threshold)
     */
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;

    /*
     * Converts starting yaw to always be 0-360 (for crossing over 0 or 360)
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      startingAngle = Robot.drivetrain.getGyroYaw() + 360;
    } else {
      startingAngle = Robot.drivetrain.getGyroYaw();
    }

    /*
     * Sets the bounds for the search mode
     */
    upperBoundAngle = startingAngle + 60;
    lowerBoundAngle = startingAngle - 60;

    /*
     * Converts bounds to always be 0-360 (for crossing over 0 or 360)
     * 
     * If the angle is negative, convert it
     */
    if (lowerBoundAngle < 0) {
      lowerBoundAngle += 360;
    }
    /* If the angle is above 360, subtract 360 */
    if (upperBoundAngle > 360) {
      upperBoundAngle -= 360;
    }
    /*
     * This will start off the command by finding whether or not a target is found, which will trigger the 
     * search mode.
     */
    targetAcquired = Robot.limelight.hasValidTargets();
  }

  /**
   * Follow the vision target. Scan if neccessary.
   */
  @Override
  protected void execute() {
    /*
     * Convert the angle constantly
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360;
    } else {
      currentAngle = Robot.drivetrain.getGyroYaw();
    }

    /*
     * Assign variables from read values from the data tables
     */
    area = Robot.limelight.getTargetArea();
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    haveTarget = Robot.limelight.hasValidTargets();

    /*
     * Combined, these two paragraphs are the searching technique that the robot is
     * going to use when the limelight doesn't detect any targets. This is the first half of the program that will
     * search a set amount of degrees (set in initilization) until a target is found. This will only run if it has
     * not been done before (!searchDone) and the init has declared no target has been found (!targetAcquired), and
     * a target has still not been found.
     */
    if (!targetAcquired) {
      if (!CCWDone) {
        /* CCW */
        Robot.drivetrain.arcadeDrive(0.0d, -speedZ);
        if (currentAngle <= (lowerBoundAngle + 5) && currentAngle >= (lowerBoundAngle - 5) && !haveTarget) {
          /* Sets CCWDone so the next paragraph can start */
          CCWDone = true;
          CWDone = false;
        }
      }
      if (CCWDone && !CWDone) {
        /* CW */
        Robot.drivetrain.arcadeDrive(0.0d, speedZ);
        if (currentAngle <= (upperBoundAngle + 5) && currentAngle >= (upperBoundAngle - 5) && !haveTarget) {
          /* Sets CWDone to true so loop stops */
          CWDone = true;
          /* Triggers the first paragraph again */
          CCWDone = false;
        }
        if (haveTarget){
          targetAcquired = true;
        }
      }
    } else {
      /*
       * This is the second half where, once the target is found, approaching will begin, or offset corrections will
       * begin, and will stop at a certain set margin relative to the center. All parameters were set in the previous
       * if statement, so this will be triggered if there was a target detected in init, detected at any time, or if
       * the search is finished.
       * 
       * Trigger offset corrections if xoffset is outside of this range, otherwise will drive to and stop at target
       */
      if (horizontalOffset > -5 && horizontalOffset < 5) {
        /* If not at target area yet, continue forwards */
        if (area < targetArea) {
          Robot.drivetrain.arcadeDrive(speedY, 0.0);
        } else if (area >= targetArea) { /* If at, or overshot target area, stop */
          Robot.drivetrain.arcadeDrive(0.0d, 0.0d);
        } 
      } else { /* Offset corrections */
        /* If offset is negative */
        if (horizontalOffset < 0) {
          /* CW Corrections */
         Robot.drivetrain.arcadeDrive(0.0d, -speedZ);
        } else if (horizontalOffset > 0) { /* If offset is positive */
          /* CCW Corrections */
          Robot.drivetrain.arcadeDrive(0.0d, speedZ);
        }
      }
    }
  }

  /**
   * Terminate when a timeout is reached.
   */
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime);
  }

  /**
   * Called once after isFinished returns true
   */
  @Override
  protected void end() {
    super.end();
    Robot.drivetrain.stop();
  }

  /**
   * Called when another command which requires one or more of the same
   * subsystems is scheduled to run
   */
  @Override
  protected void interrupted() {
    end();
  }
}

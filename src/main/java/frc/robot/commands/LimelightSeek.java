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
 * Seek for vision targets using the Limelight.
 * @author Ethan Snyder
 * @version 1.0
 * @since 1.0
 */
public class LimelightSeek extends LimelightCommand {

  private boolean haveTarget;
  private double currentAngle, lowerBoundAngle, upperBoundAngle;
  private boolean searchMode = false;
  private boolean CWDone = false;
  private boolean CCWDone = false;
  private boolean endProgram = false;
  private double speedZ;

  public LimelightSeek(double lowerBoundAngle, double upperBoundAngle, double speedZ) {
    this.speedZ = speedZ;
    this.upperBoundAngle = upperBoundAngle;
    this.lowerBoundAngle = lowerBoundAngle;
    requires(Robot.drivetrain);
  }

  /**
   * Called just before this Command runs the first time.
   */
  @Override
  protected void initialize() {
    super.initialize();

    haveTarget = Robot.limelight.hasValidTargets();

    /*Activates searchMode*/
    if (!haveTarget) {
      searchMode = true;
    }
    /*Converts bounds to always be 0-360 (for crossing over 0 or 360)*/
    if (lowerBoundAngle < 0) {
      lowerBoundAngle += 360;
    }
    /* If the angle is above 360, subtract 360 */
    if (upperBoundAngle > 360) {
      upperBoundAngle -= 360;
    }
  }

  /**
   * Called repeatedly when this Command is scheduled to run.
   */
  @Override
  protected void execute() {

    /* Convert the angle constantly */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360;
    } else {
      currentAngle = Robot.drivetrain.getGyroYaw();
    }

    /**
     * This is the good stuff, this is the moment you've all been waiting for
     */
    if (!haveTarget || searchMode) {
      if (!CCWDone && !haveTarget) {
        /* CCW */
        Robot.drivetrain.arcadeDrive(0.0d, -speedZ);
        if (currentAngle <= (lowerBoundAngle + 5) && currentAngle >= (lowerBoundAngle - 5) && !haveTarget) {
          /*Sets CCWDone true so the next paragraph can start*/
          CCWDone = true;
          /*Sets CWDone true so this can be run on a loop*/
          CWDone = false;
        }
      }
      if (CCWDone && !CWDone && !haveTarget) {
        /* CW */
        Robot.drivetrain.arcadeDrive(0.0d, speedZ);
        if (currentAngle <= (upperBoundAngle + 5) && currentAngle >= (upperBoundAngle - 5) && !haveTarget) {
          /* Sets CWDone to true so loop stops */
          CWDone = true;
          /* Triggers the first paragraph again */
          CCWDone = false;
          /* Keeps this loop from ever running again after the init triggers searchDone as false */
          searchMode = true;
        }
      }
    }
      endProgram = haveTarget;
  }

  /**
   * Make this return true when this Command no longer needs to run execute().
   */
  @Override
  protected boolean isFinished() {
    return endProgram;
  }

  /**
   * Called once after isFinished returns true
   */
  @Override
  protected void end() {
    super.end();
    Robot.drivetrain.stop();
  }

  /*
   * Called when another command which requires one or more of the same
   * subsystems is scheduled to run
   */
  @Override
  protected void interrupted() {
    end();
  }
}

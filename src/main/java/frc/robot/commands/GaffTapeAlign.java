/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.LimelightCommand;
import frc.robot.Robot;

public class GaffTapeAlign extends LimelightCommand {

  boolean hasTarget = Robot.limelight.hasValidTargets();
  boolean endProgram, CW, correctionsDone = false;
  double speedLeft, speedRight, speedRef, speedMultiplier;
  double xOffset, xOffsetLowerBound = -2, xOffsetUpperBound = 2;

  public GaffTapeAlign(double speedRef, double speedMultiplier, double xOffsetLowerBound, double xOffsetUpperBound) {
    this.speedMultiplier = speedMultiplier;
    this.xOffsetLowerBound = xOffsetLowerBound;
    this.xOffsetUpperBound = xOffsetUpperBound;
    this.speedRef = speedRef;
    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    super.initialize();
    /*Extra precautions, if the pipeline wasn't set correctly. Exits the command*/
    if (Robot.limelight.getPipe() != 1) {
      endProgram = true;
      System.out.println("Invalid pipeline, command stopping.");
    }
    /*Some fine variable work*/
    xOffset = Robot.limelight.getHorizontalOffset();
    /*Motor speeds, right has to be inverted*/
    speedRight = -speedRef;
    speedLeft = speedRef;
  }

  /*Called repeatedly when this Command is scheduled to run*/
  @Override
  protected void execute() {

    /*This will diagnose what's needed to correct. Will only run if there is a > 2 degree offset*/
    if (hasTarget) {
      xOffset = Robot.limelight.getHorizontalOffset();
      if (xOffset < 0) {
        CW = true;
      }
      if (xOffset > 0) {
        CW = false;
      }
    }
    /*This is the part that will actually do the corrections*/
    if (CW) {
      Robot.drivetrain.set(speedLeft, (speedRight * speedMultiplier));
    }
    if (!CW) {
      Robot.drivetrain.set((speedLeft * speedMultiplier), speedRight);
    }

    /*This will make the corrections stop, and now the focus will be on the encoder counts*/
    if (xOffset > xOffsetLowerBound && xOffset < xOffsetUpperBound) {
      Robot.drivetrain.set(speedLeft, speedRight);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (endProgram);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

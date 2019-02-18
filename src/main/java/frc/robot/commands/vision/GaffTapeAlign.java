/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import frc.robot.lib.vision.Limelight;
import frc.robot.lib.vision.LimelightCommand;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GaffTapeAlign extends LimelightCommand {
  double motorRevs = Robot.drivetrain.distanceToMotorRevs(10.0); 
  boolean hasTarget = Robot.limelight.hasValidTargets();
  boolean endProgram, CW, correctionsDone = false;
  double speedLeft, speedRight, speedRef, speedMultiplier;
  double xOffset, xOffsetLowerBound = -5, xOffsetUpperBound = 5;
  double currentEncoderCount, baseEncoderCountRight, baseEncoderCountLeft;

  public GaffTapeAlign(double speedRef, double speedMultiplier, double xOffsetLowerBound, double xOffsetUpperBound) {
    super(Limelight.GAFF_PIPELINE);
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

    baseEncoderCountRight = Robot.drivetrain.getRightEncoderPosition();
    baseEncoderCountLeft = Robot.drivetrain.getLeftEncoderPosition();
  
    SmartDashboard.putNumber("baseEncoderCountLeft", baseEncoderCountLeft);
    SmartDashboard.putNumber("baseEncoderCountRight", baseEncoderCountRight);

    /*Some fine variable work*/
    xOffset = Robot.limelight.getHorizontalOffset();
    /*Motor speeds, right has to be inverted*/
    speedRight = -speedRef;
    speedLeft = speedRef;
    hasTarget = Robot.limelight.hasValidTargets();
  }

  /*Called repeatedly when this Command is scheduled to run*/
  @Override
  protected void execute() {
  
    /*This will diagnose what's needed to correct. Will only run if there is a > 2 degree offset*/
    if (hasTarget) {
      xOffset = Robot.limelight.getHorizontalOffset();
      if (xOffset < 0) {
        if (xOffset > xOffsetLowerBound && xOffset < xOffsetUpperBound) {
          Robot.drivetrain.set(speedLeft, speedRight);
          currentEncoderCount = Robot.drivetrain.getRightEncoderPosition();
        }
        else {
          Robot.drivetrain.set(speedLeft, (speedRight * speedMultiplier));
          currentEncoderCount = Robot.drivetrain.getLeftEncoderPosition();
        }
      }
      else if (xOffset > 0) {
        if (xOffset > xOffsetLowerBound && xOffset < xOffsetUpperBound) {
          Robot.drivetrain.set(speedLeft, speedRight);
          currentEncoderCount = Robot.drivetrain.getRightEncoderPosition();
        }
        else {
          Robot.drivetrain.set((speedLeft * speedMultiplier), speedRight);
          currentEncoderCount = Robot.drivetrain.getRightEncoderPosition();
        }
      }
      else {
        Robot.drivetrain.set(speedLeft, speedRight);
        currentEncoderCount = Robot.drivetrain.getRightEncoderPosition();
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    
    SmartDashboard.putNumber("motorRevs", motorRevs);
    SmartDashboard.putNumber("motorRevscalc", (currentEncoderCount - baseEncoderCountLeft));

    return (endProgram || ((currentEncoderCount - baseEncoderCountLeft) >= motorRevs) || (currentEncoderCount - baseEncoderCountRight) >= motorRevs);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.stop();
    System.out.println("Motor Revs:" + motorRevs + "Total Encoder Counts Moved (Left):" + (currentEncoderCount - baseEncoderCountLeft) + "Total Encoder Counts Moved (Right):" + (currentEncoderCount - baseEncoderCountRight));
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

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
  boolean hasTarget = Robot.limelight.hasValidTargets(), endProgram;
  double speedLeft, speedRight, speedRef, speedMultiplier = 1.3d;
  double horizontalOffset, horizontalOffsetLowerBound, horizontalOffsetUpperBound, xOffsetBounds;
  boolean withinBounds = false;
  double currentEncoderCount, baseEncoderCountRight, baseEncoderCountLeft, thresholdEncoderCount, previousEncoderCount, encoderCountDifference;
  double baseTime, thresholdTime, duration;

  public GaffTapeAlign(double speedRef, double xOffsetBounds, double duration) {
    super(Limelight.GAFF_PIPELINE);
    this.xOffsetBounds = xOffsetBounds;
    this.speedRef = speedRef;
    this.duration = duration;
    requires(Robot.drivetrain);
  }

  /* Called just before this Command runs the first time */
  @Override
  protected void initialize() {
    /* Initialize the limelight */
    super.initialize();

    /* Initialize timer */
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;

    /* Extra precautions, if the pipeline wasn't set correctly. Exits the command */
    if (Robot.limelight.getPipe() != 1) {
      endProgram = true;
      System.out.println("Invalid pipeline, command stopping.");
    }

    /* Sets the first reference for encoder counts */
    baseEncoderCountLeft = Robot.drivetrain.getLeftEncoderPosition();
    baseEncoderCountRight = Robot.drivetrain.getRightEncoderPosition();

    /* Sets motor speeds based on the speedRef */
    speedRight = -speedRef;
    speedLeft = speedRef;

    /* Sets bounds based on the xOffsetBounds */
    horizontalOffsetLowerBound = (-1 * xOffsetBounds);
    horizontalOffsetUpperBound = xOffsetBounds;

    /* Figures out if there's a target or not, calculates offset, calculates threshold encoder */
    hasTarget = Robot.limelight.hasValidTargets();
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    thresholdEncoderCount = baseEncoderCountLeft + motorRevs;

    /* Printing out init values to SmartDashboard
    Robot.debug.put("thresholdEncoderCount", thresholdEncoderCount);
    Robot.debug.put("baseEncoderCountLeft", baseEncoderCountLeft);
    Robot.debug.put("baseEncoderCountRight", baseEncoderCountRight); */
  }

  /* Called repeatedly when this Command is scheduled to run */
  @Override
  protected void execute() {

    /* Kills the program if the target is lost */
    if (hasTarget) {
      endProgram = false;
    } else {
      endProgram = true;
    }

    /* Gathers the essential information, offset and encoder counts, and whether we have a target or not */
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    currentEncoderCount = Robot.drivetrain.getAverageEncoderPosition();
    hasTarget = Robot.limelight.hasValidTargets();

    /* Figures out whether or not we're within bounds, which will determine whether or not we turn */
    if (horizontalOffset > horizontalOffsetLowerBound && horizontalOffset < horizontalOffsetUpperBound) {
      withinBounds = true;
    } else {
      withinBounds = true;
    }
  
    /* If we have a target and we are offset... */
    if (hasTarget && !withinBounds) {
      /* If xOffset is negative... */
      if (horizontalOffset < 0.0d) {
        /* Speed up the left motors to turn CW */
        Robot.drivetrain.set((speedLeft * speedMultiplier), speedRight);
      } else {
        /* Otherwise speed up the right to turn CCW */
        Robot.drivetrain.set(speedLeft, (speedRight * speedMultiplier));
      }
      /* If we have a target and it is within acceptable bounds of 0 degrees... */
    } else if (hasTarget && withinBounds) {
      /* Drive both motors equally to go forwards */
      Robot.drivetrain.set(speedLeft, speedRight);
      /* And if theres no target... */
    }
  }

  /* Make this return true when this Command no longer needs to run execute() */
  @Override
  protected boolean isFinished() {
    
    /* Uncomment to print things out to SmartDashboard */
    //Robot.debug.put("motorRevs", motorRevs);
    //Robot.debug.put("motorRevscalc", (currentEncoderCount - baseEncoderCountLeft));
    //Robot.debug.put("currentEncoderCount", currentEncoderCount);

    return (endProgram || System.currentTimeMillis() > thresholdTime);
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
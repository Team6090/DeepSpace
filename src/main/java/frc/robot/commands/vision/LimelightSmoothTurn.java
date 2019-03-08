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

/**
 * Turn the robot while also moving it forwards or backwards.
 * @author Ethan Snyder, Collin Heavner
 * @version 1.0
 * @since 1.0
 */
public class LimelightSmoothTurn extends LimelightCommand {

  private double speedRef, speedLeft, speedRight, leftSpeedFinal, rightSpeedFinal;
  private long duration, baseTime, thresholdTime;
  private double currentArea, horizontalOffset, maxTargetArea;
  private boolean endProgram = false, forwardMode = false;
  private boolean CW;
  private double DEGREES_OF_ERROR = 1;
  private double variableSpeedRef, variableTurnRef;
  private double TURNING_SPEED_MULTIPLIER = 0.015d; //1.5%
  private double MAX_TURN_SPEED_REF = 0.2d;
  private double AREA_REDUCTION_FACTOR = 0.25d;

  /**
   * Set up GyroSmoothTurn.
   * @param targetArea: The area that the program will stop after hitting, preferably 20ish
   * @param duration: Timeout time
   * @param speedRef: The default speed of the thing
   */
  public LimelightSmoothTurn(long duration, double speedRef, double maxTargetArea) {
    super(Limelight.REFLECTIVE_PIPELINE);
    this.speedRef = speedRef;
    this.duration = duration;
    this.maxTargetArea = maxTargetArea;
    requires(Robot.drivetrain);
  }


  /**
   * Start the failsafe time, record the starting time. The gyro yaw
   * is zeroed out for convenience. Adjust the ranges.
   */
  @Override
  protected void initialize() {
    /*Limelight start, offset collected, endProgram made false so no restarting code required*/
    super.initialize();
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    endProgram = false;

    /* The system clock starts, timeout calculated*/
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;

    /*This will decide which motors are sped up to turn which way, determined by the boolean*/
    if (horizontalOffset > 0.0d) {
      CW = true;
    }
    else if (horizontalOffset < 0.0d) {
      CW = false;
    }
  }
  /**
   * This is saying that if the targetAngle was originally negative, which means it will now be a large
   * postive, it will not invert the motors, meaning the robot will turn counterclockwise to get to that
   * angle, the most efficient way. The else statement is saying that if the targetAngle was originally
   * positive, then it will turn clockwise, the most efficient way, to get to that angle.
   */
  @Override
  protected void execute() {
    /*
    final double STEER_K = 0.03;                    // how hard to turn toward the target
    final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
    final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast
    */
    currentArea = Robot.limelight.getTargetArea();
    horizontalOffset = Robot.limelight.getHorizontalOffset();

    variableSpeedRef = ((maxTargetArea - currentArea) * AREA_REDUCTION_FACTOR) * speedRef;
    speedLeft = variableSpeedRef;
    speedRight = -variableSpeedRef;

    if (variableSpeedRef > speedRef) {
      variableSpeedRef = speedRef;
    }

    if (horizontalOffset > 0.0d) {
      CW = true;
      variableTurnRef = horizontalOffset * TURNING_SPEED_MULTIPLIER;
      if (variableTurnRef > MAX_TURN_SPEED_REF) {
        variableTurnRef = MAX_TURN_SPEED_REF;
      }
      leftSpeedFinal = (speedLeft + variableTurnRef);
    }
    else if (horizontalOffset < 0.0d) {
      CW = false;
      variableTurnRef = horizontalOffset * TURNING_SPEED_MULTIPLIER;
      if (variableTurnRef < (-1 * MAX_TURN_SPEED_REF)) {
        variableTurnRef = (-1 * MAX_TURN_SPEED_REF);
      }
      rightSpeedFinal = (speedRight + variableTurnRef);
    }

    /*This will make the motors turn the detemined amount and speeds set in the init class*/
    if (!forwardMode)
      if (CW) {
       Robot.drivetrain.set(leftSpeedFinal, speedRight);
      }
      else if (!CW) {
      Robot.drivetrain.set(speedLeft, rightSpeedFinal);
      }
      if (horizontalOffset < DEGREES_OF_ERROR && horizontalOffset > -DEGREES_OF_ERROR) {
       forwardMode = true;
      }
      else {
        forwardMode = false;
      }
    if (forwardMode) {
      Robot.drivetrain.set(speedLeft, speedRight);
      if (currentArea > maxTargetArea) {
        endProgram = true;
      }
    }
  }

  /**
   * Either stop upon timeout or upon endProgram
   */
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime || endProgram);
  }

  /**
   * Stops the motors when the program is terminated, and prints out the ending values for timeout.
   */
  @Override
  protected void end() {
    super.end();
    Robot.drivetrain.stop();
    System.out.println("Time elapsed: " + (System.currentTimeMillis() - baseTime));
  }

  /**
   * Finish this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}
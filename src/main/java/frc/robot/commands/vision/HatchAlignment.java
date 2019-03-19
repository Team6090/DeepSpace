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
public class HatchAlignment extends LimelightCommand {

  private double speedRef, speedLeft, speedRight, leftSpeedFinal, rightSpeedFinal;
  private long duration, baseTime, thresholdTime;
  private double currentArea, horizontalOffset, maxTargetArea;
  private boolean endProgram = false, forwardMode = false;
  private boolean CW;
  private double DEGREES_OF_ERROR = 1;
  private double variableSpeedRef, variableTurnRef;
  private double TURNING_SPEED_MULTIPLIER = 0.015d;
  private double MAX_TURN_SPEED_REF = 0.2d;
  private double MAX_VARIABLE_SPEED_REF = 0.25d;

  private double AREA_REDUCTION_FACTOR = 0.1d;

  /**
   * Set up GyroSmoothTurn.
   * @param maxTargetArea: The area that the program will stop after hitting, preferably 20ish
   * @param duration: Timeout time
   * @param speedRef: The default speed of the thing
   */
  public HatchAlignment(long duration, double speedRef, double maxTargetArea) {
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
    /* Limelight start, endProgram is set to false just in case */
    super.initialize();
    horizontalOffset = Robot.limelight.getHorizontalOffset();
    endProgram = false;

    /* The system clock starts, timeout calculated*/
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;

    /**
     * Determines CW or CCW turning, or, in other words, will overall determine which side of the motors
     * are sped up and which side remains unchanged. This works by using tx from the limelight, which is the 
     * horizontalOffset
     */
    if (horizontalOffset > 0.0d) {
      CW = true;
    }
    else if (horizontalOffset < 0.0d) {
      CW = false;
    }
  }
  /**
   * Execute is pretty packed, but what's happening in execute is all the calculation that ease the robots
   * speed, whether that's turning or driving forward. These things are all going to slow down with every 
   * little change in the robots ta or tx, so it needs to be able to iterate itself to constantly calculate
   * the variableSpeedRef and the variableTurnRef.
   */
  @Override
  protected void execute() {

    /* Grabs a few necessary values from the limelight for calculations */
    currentArea = Robot.limelight.getTargetArea();
    horizontalOffset = Robot.limelight.getHorizontalOffset();

    /**
     * These are the area calculations that will ultimately affect the motor speed, which means it will slow
     * itself down as the limelights ta (currentArea) nears the maxTargetArea. speedRight is inverted
     * simply because of how it's arranged on the robot, it needs to run reversed.
     */
    variableSpeedRef = ((maxTargetArea - currentArea) * AREA_REDUCTION_FACTOR) * speedRef;
    speedLeft = variableSpeedRef;
    speedRight = -variableSpeedRef;

    /**
     * This keeps variableSpeedRef from getting too high, and will regulate to always be under a constant,
     * MAX_VARIABLE_SPEED_REF, set at the top of HatchAlignment
     */
    if (variableSpeedRef > MAX_VARIABLE_SPEED_REF) {
      variableSpeedRef = MAX_VARIABLE_SPEED_REF;
    }

    /** 
     * These few if statements handle the math behind the turning. These if statements ultimately end up
     * calculating variableTurnRef, which is then ADDED onto the speedRef for the motor that, determined in
     * the init, will be turning spinning faster for the other, which will make the robot actually turn.
     */
    /* Figures out whether the robot will turn CW and then multiplies by a constant */
    if (horizontalOffset > 0.0d) {
      CW = true;
      variableTurnRef = horizontalOffset * TURNING_SPEED_MULTIPLIER;
      /* Keeps variableTurnRef in check by keeping it under a set constant */
      if (variableTurnRef > MAX_TURN_SPEED_REF) {
        variableTurnRef = MAX_TURN_SPEED_REF;
      }

      /* Calculates leftSpeedFinal using the default speed and the calculated variableTurnRef */
      leftSpeedFinal = (speedLeft + variableTurnRef);

    } else if (horizontalOffset < 0.0d) {
      CW = false;
      variableTurnRef = horizontalOffset * TURNING_SPEED_MULTIPLIER;
      /* Keeps variableTurnRef in check, keeps it below a set constant */
      if (variableTurnRef < (-1 * MAX_TURN_SPEED_REF)) {
        variableTurnRef = (-1 * MAX_TURN_SPEED_REF);
      }
      /* Calculates rightSpeedFinal using the default speed and the calculated variableTurnRef */
      rightSpeedFinal = (speedRight + variableTurnRef);
    }

    /**
     * This is the paragraph that will finally put together all the hard work that came before it, this is 
     * going to put all of the values on the motors and turn the motors correctly, CW or CCW
     */
    if (!forwardMode)
      if (CW) {
       Robot.drivetrain.set(leftSpeedFinal, speedRight);
      } else if (!CW) {
      Robot.drivetrain.set(speedLeft, rightSpeedFinal);
      }
      /* This is our forwardMode trigger, if we're within a set variance of tx, turning will cease */
      if (horizontalOffset < DEGREES_OF_ERROR && horizontalOffset > -DEGREES_OF_ERROR) {
       forwardMode = true;
      } else {
        forwardMode = false;
      }
    /* This simply says that when the turning is done, forwardMode is true, both motors will go the base value */
    if (forwardMode) {
      Robot.drivetrain.set(speedLeft, speedRight);
      if (currentArea >= maxTargetArea) {
        endProgram = true;
      }
    }
  }

  /**
   * Either stop upon timeout or upon endProgram, which is set true in many different ways
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
    //super.end();
    Robot.drivetrain.stop();
  }

  /**
   * Finish this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}
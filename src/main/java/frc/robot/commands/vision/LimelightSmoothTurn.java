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

  private double speedRef, speedLeft, speedRight, leftSpeedFinal, rightSpeedFinal, speedMultiplier = 1.4d;
  private long duration, baseTime, thresholdTime;
  private double area, horizontalOffset, targetArea;
  private boolean endProgram = false, forwardMode = false;
  private boolean CW;

  /**
   * Set up GyroSmoothTurn.
   * @param targetArea: The area that the program will stop after hitting, preferably 20ish
   * @param duration: Timeout time
   * @param speedRef: The default speed of the thing
   */
  public LimelightSmoothTurn(long duration, double speedRef, double targetArea, double speedMultiplier) {
    super(Limelight.REFLECTIVE_PIPELINE);
    this.speedRef = speedRef;
    this.duration = duration;
    this.targetArea = targetArea;
    this.speedMultiplier = speedMultiplier;
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
    speedLeft = speedRef;
    speedRight = -speedRef;
    if (horizontalOffset > 0.0d) {
      CW = true;
      leftSpeedFinal = (speedLeft * speedMultiplier);
    }
    else if (horizontalOffset < 0.0d) {
      CW = false;
      rightSpeedFinal = (speedRight * speedMultiplier);

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

    area = Robot.limelight.getTargetArea();
    horizontalOffset = Robot.limelight.getHorizontalOffset();

    /*This will make the motors turn the detemined amount and speeds set in the init class*/
    if (!forwardMode)
      if (CW) {
       Robot.drivetrain.set(leftSpeedFinal, speedRight);
      }
      else if (!CW) {
      Robot.drivetrain.set(speedLeft, rightSpeedFinal);
      }
      if (horizontalOffset < 2.0d && horizontalOffset > -2.0d) {
       forwardMode = true;
      }
      else {
        forwardMode = false;
      }
    if (forwardMode) {
      Robot.drivetrain.set(speedLeft, speedRight);
      if (area > targetArea) {
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
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
 * Turn the robot while also moving it forwards or backwards.
 * @author Ethan Snyder, Collin Heavner
 * @version 1.0
 * @since 1.0
 */
public class LimelightSmoothTurn extends LimelightCommand {

  private double speedRef, speedLeft, speedRight;
  private long duration, baseTime, thresholdTime;
  private double area, horizontalOffset, targetArea;
  private boolean endProgram = false;
  private double leftSpeedFinal, rightSpeedFinal;
  private boolean CW;
  private boolean forwardMode = false;

  /**
   * Set up GyroSmoothTurn.
   * @param inputAngle The goal angle that the robot is going to turn to
   * @param duration The time in which the timer will stop the turn after
   * @param speedLeft The twisting direction of the joystick which will actually make the bot turn
   * @param speedRight The forwards and backwards joystick inputs which will make the bot go vroom
   */
  public LimelightSmoothTurn(long duration, double speedRef, double targetArea) {
    this.speedRef = speedRef;
    this.duration = duration;
    this.targetArea = targetArea;
    requires(Robot.drivetrain);
  }


  /**
   * Start the failsafe time, record the starting time. The gyro yaw
   * is zeroed out for convenience. Adjust the ranges.
   */
  @Override
  protected void initialize() {
    super.initialize();
    endProgram = false;
    
    speedLeft = speedRef;
    speedRight = -speedRef;

    horizontalOffset = Robot.limelight.getHorizontalOffset();

    /* The system clock starts. */
    baseTime = System.currentTimeMillis();
    /* Determines time that robot has to timeout after */
    thresholdTime = baseTime + duration;

    /*This will decide which motors are sped up to turn which way, determined by the boolean*/
    if (horizontalOffset > 0) {
      CW = true;
      leftSpeedFinal = (speedLeft * 1.4);
    }
    else if (horizontalOffset < 0) {
      CW = false;
      rightSpeedFinal = (speedRight * 1.4);

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
      if (horizontalOffset < 2 && horizontalOffset > -2) {
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
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Rotate the robot using the gyro.
 * @author Ethan Snyder
 * @version 1.0
 * @since 1.0
 */
public class Rotate extends Command {

  float inputAngle, startingAngle, totalAngleTurn, currentAngle;
  
  double speedZ;
  double speedY = 0.0d;

  long duration, baseTime, thresholdTime;

  boolean clockwise;
  boolean specialCase = false;

  /**
   * Set up GyroRotate.
   * @param angle The goal angle that the robot is going to turn to
   * @param timeout The time in which the timer will stop the turn after
   * @param speedZ The twisting direction of the joystick which will actually make the bot turn
   * @param speedY The forwards and backwards joystick inputs which will make the bot go vroom
   */
  public Rotate(float inputAngle, long duration, double speedZ, double speedY) {
    this.speedY = speedY;
    this.speedZ = speedZ;
    this.inputAngle = inputAngle;
    this.duration = duration;
    requires(Robot.drivetrain);
  }

  /**
   * The first part of this starts the failsafe time, it records the starting time. After that, the gyro yaw
   * is zeroed out for convenience. The next two if statements are basically adjusting the ranges 
   */
  @Override
  protected void initialize() {

    /* System clock starts */
    baseTime = System.currentTimeMillis();
    /* Determines time that robot has to timeout after */
    thresholdTime = baseTime + duration;

    /*
     * Convert the starting angle
     */
    if (Robot.drivetrain.getGyroYaw() < 0.0f) {
      /* Negative turn to 180-360 */
      startingAngle = Robot.drivetrain.getGyroYaw() + 360.0f;
    } else {
      /* Positives stay 0-180 */
      startingAngle = Robot.drivetrain.getGyroYaw();
    }

    /* The total angle needed to turn */
    totalAngleTurn = startingAngle + inputAngle;

    /*
     * Declare the turning direction of special cases when the angle passes over 0 or 360
     */
    if (totalAngleTurn < 0.0f) { 
      /* If the angle is negative, convert it */
      totalAngleTurn += 360.0f;
      /* Trigger special case */
      specialCase = true;
      /* Trigger counterclockwise */
      clockwise = false;
    }
    if (totalAngleTurn > 360.0f) {
      /* If the angle is above 360, subtract 360 */
      totalAngleTurn -= 360.0f;
      /* Trigger special case */
      specialCase = true;
      /* Trigger clockwise */
      clockwise = true;
    }
    /*
     * Only run if the special cases do not already declare a turning direction
     */
    if (!specialCase) {
      /* If the input angle is bigger, turn clockwise */
      if (totalAngleTurn > startingAngle) {
        clockwise = true;
      }
      /* If the input angle is smaller, turn counterclockwise */
      if (totalAngleTurn < startingAngle) {
        clockwise = false;
      }
    }
  }

  /**
   * If the targetAngle was originally negative, which means it will now be a large
   * postive, it will not invert the motors, meaning the robot will turn counterclockwise to get to that
   * angle, the most efficient way.
   * 
   * If the targetAngle was originally positive, then it will turn clockwise, the most efficient way, to
   * get to that angle.
   */
  @Override
  protected void execute() {
    /*
     * Keep the angle read from the gyro constantly converted as long as currentAngle is 
     * referenced compared to always reading raw values from the getGyroYaw.
     */
    if (Robot.drivetrain.getGyroYaw() < 0.0f) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360.0f;
    } else {
      currentAngle = Robot.drivetrain.getGyroYaw();

    }
    if (!clockwise) {
      /* Counterclockwise turning */
      Robot.drivetrain.arcadeDrive(speedY, -speedZ);
    } else if (clockwise) {
      /* Clockwise turning */
      Robot.drivetrain.arcadeDrive(speedY, speedZ);
    }
  }

  /**
   * When the yaw of the robot is within a range of the target angle, or the time is up, finish.
   */
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime || (currentAngle > (totalAngleTurn - 2) && currentAngle < (totalAngleTurn + 2)));
  }

  /**
   * Stop the motors and print out the ending values for time and angles.
   */
  @Override
  protected void end() {
    Robot.drivetrain.stop();
    System.out.println("Time elapsed: " + (System.currentTimeMillis() - baseTime) + ", and total angle turned: " + (currentAngle - startingAngle));
  }

  /**
   * End this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}
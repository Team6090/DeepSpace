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
 * Turn the robot while also moving it forwards or backwards.
 * @author Ethan Snyder
 * @version 1.0
 * @since 1.0
 */
public class GyroSmoothTurn extends Command {

  double inputAngle, startingAngle, totalAngleTurn, currentAngle;
  
  double speedZ;
  double speedY;

  long duration, baseTime, thresholdTime;

  boolean clockwise;
  boolean specialCase = false;

  /**
   * Set up GyroSmoothTurn.
   * @param inputAngle The goal angle that the robot is going to turn to
   * @param duration The time in which the timer will stop the turn after
   * @param speedZ The twisting direction of the joystick which will actually make the bot turn
   * @param speedY The forwards and backwards joystick inputs which will make the bot go vroom
   */
  public GyroSmoothTurn(double inputAngle, long duration, double speedZ, double speedY) {
    this.speedY = speedY;
    this.speedZ = speedZ;
    this.inputAngle = inputAngle;
    this.duration = duration;
    requires(Robot.drivetrain);
  }

  /**
   * Start the failsafe time, record the starting time. The gyro yaw
   * is zeroed out for convenience. Adjust the ranges.
   */
  @Override
  protected void initialize() {

    /* The system clock starts. */
    baseTime = System.currentTimeMillis();
    /* Determines time that robot has to timeout after */
    thresholdTime = baseTime + duration;

    /*
     * This will convert the starting angle
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      /* Negative turn to 180-360 */
      startingAngle = Robot.drivetrain.getGyroYaw() + 360;
    } else {
      /* Positives stay 0-180 */
      startingAngle = Robot.drivetrain.getGyroYaw();
    }

    /* Sets the total angle needed to turn */
    totalAngleTurn = startingAngle + inputAngle;

    /*
     * This will declare the turning direction of special cases when the angle passes over 0 or 360
     */
    if (totalAngleTurn < 0) {
      /* If the angle is negative, convert it  */ 
      totalAngleTurn += 360;
      /* Trigger special case */
      specialCase = true;
      /* Trigger counterclockwise */
      clockwise = false;
    }
    if (totalAngleTurn > 360) {
      /* If the angle is above 360, subtract 360 */
      totalAngleTurn -= 360;
      /* Trigger special case */
      specialCase = true;
      /* Trigger clockwise */
      clockwise = true;
    }
    /*
     * This will only run if the special cases do not already declare a turning direction
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
   * This is saying that if the targetAngle was originally negative, which means it will now be a large
   * postive, it will not invert the motors, meaning the robot will turn counterclockwise to get to that
   * angle, the most efficient way. The else statement is saying that if the targetAngle was originally
   * positive, then it will turn clockwise, the most efficient way, to get to that angle.
   */
  @Override
  protected void execute() {
    /*
     * This should keep the angle read from the gyro constantly converted as long as currentAngle is 
     * referenced compared to always reading raw values from the getGyroYaw.
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360;
    }
    else {
      currentAngle = Robot.drivetrain.getGyroYaw();

    }
    if (!clockwise) {
      /* CCW turning */
      Robot.drivetrain.arcadeDrive(speedY, -speedZ);
    }
    else if (clockwise) {
      /* Clockwise turning */
      Robot.drivetrain.arcadeDrive(speedY, speedZ);
    }
  }

  /**
   * This is going to set a range for the termination thingy, meaning that when the yaw of the robot is within
   * a range of the target angle, the program will kill itself
   */
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime || (currentAngle > (totalAngleTurn - 2) && currentAngle < (totalAngleTurn + 2)));
  }

  /**
   * Stops the motors when the program is terminated, and prints out the ending values for time and
   * angles.
   */
  @Override
  protected void end() {
    Robot.drivetrain.arcadeDrive(0, 0);
    System.out.println("Time elapsed: " + (System.currentTimeMillis() - baseTime) + ", and total angle turned: " + (currentAngle - startingAngle));
  }

  /**
   * Finish this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}
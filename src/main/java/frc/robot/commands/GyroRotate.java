/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/*
 * This here thingy is going to turn the robot.
 * 
 * @author Ethan Snyder
 * @version 1.0
 * @since 1.0
 */
public class GyroRotate extends Command {

  float yaw;

  double inputAngle, startingAngle, totalAngleTurn, currentAngle;
  double thresholdangle;
  
  double speedZ;
  double speedY = 0;

  long duration, baseTime, thresholdTime;

  boolean clockwise;
  boolean endProgram;
  boolean specialCase;

  /**
   * @param angle The goal angle that the robot is going to turn to
   * @param timeout The time in which the timer will stop the turn after
   * @param speedZ The twisting direction of the joystick which will actually make the bot turn
   * @param speedY The forwards and backwards joystick inputs which will make the bot go vroom
   */
  public GyroRotate(double inputAngle, long duration, double speedZ, double speedY) {
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

    baseTime = System.currentTimeMillis(); //System clock starts
    thresholdTime = baseTime + duration;  //Determines time that robot has to timeout after

    /**
     * This will convert the starting angle
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      startingAngle = Robot.drivetrain.getGyroYaw() + 360; //Negative turn to 180-360
    }
    else {
      startingAngle = Robot.drivetrain.getGyroYaw(); //Positives stay 0-180
    }

    totalAngleTurn = startingAngle + inputAngle; //Sets the total angle needed to turn

    if (totalAngleTurn < 0) { 
      totalAngleTurn += 360; //If the angle is negative, convert it 
      specialCase = true;  //Trigger special case
      clockwise = false; //Trigger clockwise
    }
    if (totalAngleTurn > 360) {
      totalAngleTurn -= 360; //If the angle is above 360, subtract 360
      specialCase = true; //Trigger special case
      clockwise = true; //Trigger counterclockwise
    }

    /**
     * This will only run if the special cases do not already declare a turning direction
     */
    if (!specialCase)
      if (totalAngleTurn > startingAngle) { //If the input angle is bigger, turn clockwise
        clockwise = true;
    }
      if (totalAngleTurn < startingAngle) { //If the input angle is smaller, turn counterclockwise
        clockwise = false;
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
    System.out.println("Raw Gyro Angle: " + Robot.drivetrain.getGyroYaw());
    System.out.println("Converted Gyro Angle: " + currentAngle);
    /**
     * This should keep the angle read from the gyro constantly converted as long as currentAngle is 
     * referenced compared to always reading raw values from the getGyroYaw.
     */
    if (Robot.drivetrain.getGyroYaw() < 0) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360;
    }
    else {
      currentAngle = Robot.drivetrain.getGyroYaw();
    }
    /**
     * CCW Turning
     */
    if (!clockwise) {
      Robot.drivetrain.arcadeDrive(speedY, -speedZ);
    }
    /**
     * CW Turning
     */
    else {
      Robot.drivetrain.arcadeDrive(speedY, speedZ);
    }
  }
  /**
   * The first part of the condition here is that if the current system time is above the time treshhold,
   * the program will stop. The second part is basically saying:
   * targetAngleMin < Current Yaw < targetAngleMax
   * Except java, as far as I know, doesn't accept it like that, so I had to make it && to accept that range.
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

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
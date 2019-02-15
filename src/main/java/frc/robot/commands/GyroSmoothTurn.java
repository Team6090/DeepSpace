/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Turn the robot while also moving it forwards or backwards.
 * @author Ethan Snyder, Collin Heavner
 * @version 1.0
 * @since 1.0
 */
public class GyroSmoothTurn extends Command {

  double currentAngle, targetAngle;
  double speedRef, speedLeft, speedRight;
  long duration, baseTime, thresholdTime;
  double area, tx, tv, targetArea;
  boolean endProgram = false;
  double leftSpeedFinal, rightSpeedFinal;
  boolean CW;
  boolean forwardMode = false;

  /**
   * Set up GyroSmoothTurn.
   * @param inputAngle The goal angle that the robot is going to turn to
   * @param duration The time in which the timer will stop the turn after
   * @param speedLeft The twisting direction of the joystick which will actually make the bot turn
   * @param speedRight The forwards and backwards joystick inputs which will make the bot go vroom
   */
  public GyroSmoothTurn(long duration, double speedRef, double targetArea) {
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

    speedLeft = speedRef;
    speedRight = -speedRef;

    //double area = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    
    if (tv == 0) {
      endProgram = true;
    }

    /* The system clock starts. */
    baseTime = System.currentTimeMillis();
    /* Determines time that robot has to timeout after */
    thresholdTime = baseTime + duration;

    /**
     * This will decide which motors are sped up to turn which way, determined by the boolean
     */
    if (tx < 0) {
      CW = false;
      leftSpeedFinal = speedLeft * 1.4;
    }
    if (tx > 0) {
      CW = true;
      rightSpeedFinal = speedRight * 1.4;
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
     * Assign variables from read values from the data tables
     */
    double area = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    //double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
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
    /*
     * This will make the motors turn the detemined amount and speeds set in the init class
     */
    if (!forwardMode)
      if (CW) {
       Robot.drivetrain.set(speedLeft, rightSpeedFinal);
      }
      if (!CW) {
      Robot.drivetrain.set(leftSpeedFinal, speedRight);
      }
      if (tx < 1 && tx > 1) {
       currentAngle = targetAngle;
       forwardMode = true;
    }
    if (forwardMode) {
      Robot.drivetrain.set(speedLeft, speedRight);
      if (area > targetArea) {
        endProgram = true;
      }
    }
  }
  /**
   * This is going to set a range for the termination thingy, meaning that when the yaw of the robot is within
   * a range of the target angle, the program will kill itself
   */
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime || endProgram);
  }

  /**
   * Stops the motors when the program is terminated, and prints out the ending values for time and
   * angles.
   */
  @Override
  protected void end() {
    Robot.drivetrain.arcadeDrive(0, 0);
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
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

public class PuppyDog extends Command {

  double area, tx, tv, targetArea;
  double speedY, speedZ;
  double baseTime, thresholdTime, duration;
  double startingAngle, currentAngle, upperBoundAngle, lowerBoundAngle;
  boolean specialCaseCW = false;
  boolean specialCaseCCW = false;
  boolean scannerMode = false;

  public PuppyDog(double speedY, double speedZ, double targetArea, double duration) {
    this.speedY = speedY;
    this.speedZ = speedZ;
    this.targetArea = targetArea;
    this.duration = duration;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    baseTime = System.currentTimeMillis();
    thresholdTime = baseTime + duration;

    if (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0) == 0) {
      scannerMode = true;
    }

    if (Robot.drivetrain.getGyroYaw() < 0) {
      startingAngle = Robot.drivetrain.getGyroYaw() + 360;
    }
    else {
      startingAngle = Robot.drivetrain.getGyroYaw();
    }

    upperBoundAngle = startingAngle + 60;
    lowerBoundAngle = startingAngle - 60;

    if (lowerBoundAngle < 0) { 
      lowerBoundAngle += 360; //If the angle is negative, convert it
      specialCaseCCW = true;
    }
    if (upperBoundAngle > 360) {
      upperBoundAngle -= 360; //If the angle is above 360, subtract 360
      specialCaseCW = true;
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.drivetrain.getGyroYaw() < 0) {
      currentAngle = Robot.drivetrain.getGyroYaw() + 360;
    }
    else {
      currentAngle = Robot.drivetrain.getGyroYaw();
    }
    double area =  NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

    if (scannerMode) {
      Robot.drivetrain.arcadeDrive(0.0d, -speedZ); //CCW
      if (currentAngle <= (lowerBoundAngle + 5) && currentAngle >= (lowerBoundAngle - 5)) {
        
      } 
    }
    if (tv == 1 && area <= targetArea) {
      Robot.drivetrain.arcadeDrive(speedY, 0.0); 
    }
    if (tv == 1 && area >= targetArea) {
      Robot.drivetrain.arcadeDrive(0.0d, 0.0d);
    }
    if (tx >= 15) {
      //CW
    }
    if (tx <= 15) {
      //CCW
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (System.currentTimeMillis() >= thresholdTime);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PuppyDog extends Command {

  double area, x, y, tv, targetArea;
  double speedRight, speedLeft;

  public PuppyDog(double speedRight, double speedLeft, double targetArea) {
    this.speedRight = speedRight;
    this.speedLeft = speedLeft;
    this.targetArea = targetArea;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double area =  NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    double x = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);

    while (tv == 0) {
      Robot.drivetrain.set(0.3, -0.3);
    }
    while (tv == 1 && area <= targetArea) {
      Robot.drivetrain.set(speedLeft, speedRight);
      
    }
    while (tv == 1 && area >= targetArea) {
      Robot.drivetrain.set(0.0d, 0.0d);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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

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
 * Control the drivetrain with the joystick.
 */
public class DriveWithJoystick extends Command {
  public DriveWithJoystick() {
    requires(Robot.drivetrain);
  }

  @Override
  protected void execute() {
    /*
     * We pull the throttled values, which are automatically calculated by the slider
     * value.
     */
    Robot.drivetrain.arcadeDrive(Robot.oi.getThrottledY(), Robot.oi.getThrottledZ());
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }
}

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
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class DriveWithJoystick extends Command {
  /**
   * Construct the DriveWithJoystick command. This command requires the
   * drivetrain subsystem.
   */
  public DriveWithJoystick() {
    requires(Robot.drivetrain);
  }

  /**
   * Pull values from the OI and pass them directly to the drivetrain motors.
   */
  @Override
  protected void execute() {
    /*
     * We pull the throttled values, which are automatically calculated by the slider
     * value.
     */
    Robot.drivetrain.arcadeDrive(Robot.oi.getThrottledY() * -1.0, Robot.oi.getThrottledZ());
  }

  /**
   * This command will run until it is interrupted.
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Stop the drivetrain.
   */
  @Override
  protected void end() {
    Robot.drivetrain.stop();
  }

  /**
   * End execution of this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

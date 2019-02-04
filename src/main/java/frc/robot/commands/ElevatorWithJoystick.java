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
 * Control the elevator with the joystick.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class ElevatorWithJoystick extends Command {
  /**
   * Construct the ElevatorWithJoystick command. This command requires
   * the elevator subystem.
   */
  public ElevatorWithJoystick() {
    requires(Robot.elevator);
  }

  /**
   * Pass the XBox controller vertical joystick value directly into the
   * elevator motor.
   */
  @Override
  protected void execute() {
    Robot.elevator.setSpeed(Robot.oi.xBoxLeftJoystickVertical());
  }

  /**
   * This command will run until it is interrupted.
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Stop the elevator.
   */
  @Override
  protected void end() {
    Robot.elevator.stop();
  }

  /**
   * End this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

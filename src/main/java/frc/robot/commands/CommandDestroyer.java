/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.commands.joystick.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.Robot;

/**
 * Destroy all currently running commands and replace them with the Joystick commands.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class CommandDestroyer extends Command {
  /**
   * Require all Subsystems so that when this is run,
   * anything else using these subsystems will be stopped.
   */
  public CommandDestroyer() {
    requires(Robot.drivetrain);
    requires(Robot.elevator);
    requires(Robot.intake);
  }

  /**
   * Clear the scheduler, disable it, re-enable it, then add
   * the commands that use the joystick.
   */
  @Override
  protected void initialize() {
    Scheduler s = Scheduler.getInstance();
    s.removeAll();
    s.disable();
    s.enable();
    s.add(new DriveWithJoystick());
    s.add(new ElevatorController());
    s.add(new IntakeWithJoystick());
    s.run();
  }

  /**
   * After init, this command is done.
   */
  @Override
  protected boolean isFinished() {
    return true;
  }

  /**
   * Confirm that the command has completed.
   */
  @Override
  protected void end() {
    System.out.println("All Commands and Subsystems reset. Joystick control resumed.");
  }

  /**
   * This should theoretically never happen.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

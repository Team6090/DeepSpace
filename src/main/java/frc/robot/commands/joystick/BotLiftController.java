/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * BotLift: Lift the robot up on the press of a button.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class BotLiftController extends Command {

  /**
   * This command requires the base subsystem.
   */
  public BotLiftController() {
    requires(Robot.base);
  }

  /**
   * Constantly check the state of the button and set the lift to
   * that state.
   */
  @Override
  protected void execute() {
    if (Robot.oi.getJoystickButton(6)) {
      Robot.base.liftBot(!Robot.base.isLifted());
    }
  }

  /**
   * This command will not stop until it is interrupted.
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Return the bot to the ground when this command ends.
   */
  @Override
  protected void end() {
    Robot.base.liftBot(false);
  }

  /**
   * On interrupt, end the command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

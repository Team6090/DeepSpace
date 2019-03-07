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
 * Toggle the bot lifting mechanism.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class LiftStateShift extends Command {

  /**
   * This command requires the base subsystem.
   */
  public LiftStateShift() {
    requires(Robot.base);
  }


  @Override
  protected void initialize() {
    Robot.base.liftBot(!Robot.base.isLifted());
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
    end();
  }
}

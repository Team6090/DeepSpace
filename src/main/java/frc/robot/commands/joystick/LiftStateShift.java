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
   * The possible sets of pneumatics to lift.
   */
  public static enum LiftSet {
    REAR, FRONT
  }

  /* The lift set that this command was instantiated with. */
  private LiftSet liftSet;

  /**
   * This command requires the base subsystem.
   */
  public LiftStateShift(LiftSet liftSet) {
    requires(Robot.base);
    this.liftSet = liftSet;
  }


  /**
   * Do the lift operation depending on the current state of the robot base
   * and the parameters provided.
   */
  @Override
  protected void initialize() {
    if (liftSet == LiftSet.FRONT) {
      Robot.base.liftFront(!Robot.base.frontIsLifted());
    } else if (liftSet == LiftSet.REAR) {
      Robot.base.liftRear(!Robot.base.rearIsLifted());
    } else {
      System.out.println("LiftStateShift: Illegal State provided, no action will be taken.");
    }
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

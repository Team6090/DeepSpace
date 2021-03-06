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
 * Control the intake with the joystick.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class IntakeWithJoystick extends Command {

  /**
   * Construct the IntakeWithJoystick command. This command requires
   * the intake subystem.
   */
  public IntakeWithJoystick() {
    requires(Robot.intake);
  }

  /**
   * The following functions are handled here:
   * - Check the state of the XBox POV, and set the intake arm position accordingly.
   * - Pass the XBox trigger values into the intake motor.
   * - Trigger the hatch release if the right bumper is pressed.
   */
  @Override
  protected void execute() {
    /* Intake arm - POV Up: arm up, POV down: arm down. */
    switch (Robot.oi.xBoxPOV()) {
      case UP:
        Robot.intake.armUp();
        break;
      case DOWN:
        Robot.intake.armDown();
        break;
      default:
        break;
    }
    
    /* Intake motor - Left trigger: intake in, right trigger: intake out. */
    double leftTriggerValue = Robot.oi.xBoxLeftTrigger();
    double rightTriggerValue = Robot.oi.xBoxRightTrigger();
    if (leftTriggerValue > 0.0d) {
      Robot.intake.setSpeed(leftTriggerValue);
    } else if (rightTriggerValue > 0.0d) {
      /* Eject at the raw speed. */
      Robot.intake.setSpeed(rightTriggerValue * -1.0d);
    } else {
      Robot.intake.stop();
    }

    /* If either bumper is pressed, fire the hatch release solenoid. */
    if (Robot.oi.xBoxLeftBumper() || Robot.oi.xBoxRightBumper()) {
      Robot.intake.hatchRelease(true);
    } else {
      Robot.intake.hatchRelease(false);
    }
  }

  /**
   * This command will be run until it is interrupted.
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Put the arm up, and stop the intake motors.
   */
  @Override
  protected void end() {
    Robot.intake.armUp();
    Robot.intake.stop();
  }

  /**
   * End this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

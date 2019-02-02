/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class IntakeWithJoystick extends Command {
  public IntakeWithJoystick() {
    requires(Robot.intake);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
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
    
    double leftTriggerValue = Robot.oi.xBoxLeftTrigger();
    double rightTriggerValue = Robot.oi.xBoxRightTrigger();
    if (leftTriggerValue > 0.0) {
      /* Multiply by -1 to intake. */
      Robot.intake.setSpeed(leftTriggerValue * -1.0);
    } else if (rightTriggerValue > 0.0) {
      Robot.intake.setSpeed(rightTriggerValue);
    } else {
      Robot.intake.stop();
    }

    if (Robot.oi.xBoxLeftBumper()) {
      /* ... */
    } else if (Robot.oi.xBoxRightBumper()) {
      /* ... */
    } else {
      /* ... */
    }

  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    Robot.intake.armUp();
    Robot.intake.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }
}

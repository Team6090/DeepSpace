/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.command.Command ;
import frc.robot.Robot;

public class VisionTracker extends Command {
  private final Command hatchAlignment = new HatchAlignment(5000l, 0.5d, 8.00d);
  private final Command gaffTapeAlign = new GaffTapeAlign(0.5d, 2.0d, 5000);
  private Command currentCommand;
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    if (Robot.intake.armIsUp()) {
      currentCommand = hatchAlignment;
    } else {
      currentCommand = gaffTapeAlign;
    }
    currentCommand.start();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    currentCommand.cancel();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}

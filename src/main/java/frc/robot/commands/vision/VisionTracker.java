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
    /* If the intake arm is up... */
    if (Robot.intake.armIsUp()) {
      /* We run hatchAlignment */
      currentCommand = hatchAlignment;
    } else {
      /* Otherwise, if it's down, the button runs gaffTapeAlign */
      currentCommand = gaffTapeAlign;
    }
    /* Then the command, once decided what will run, will run */
    currentCommand.start();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    /* 
     * Since the commands are set to be whileheld, this function and the interrupt thing will handle the 
     * program being killed by the button being let off
     */
    currentCommand.cancel();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}

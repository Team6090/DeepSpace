/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.vision.GaffTapeAlign;
import frc.robot.commands.vision.HatchAlignment;

public class VisionTracker extends CommandGroup {

  //private boolean armUp = Robot.intake.armIsUp();

  //if (armUp) {
  //  addSequential(new HatchAlignment(10000l, 0.5d, 8.00d));
  //}

  public VisionTracker(boolean armUp) {
    if (armUp) {
      addSequential(new HatchAlignment(10000l, 0.5d, 8.00d));
    } else {
    addSequential(new GaffTapeAlign(.4, 1.3, -3, 3, 1, 5000));
    }
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.DriveForward;

/**
 * A test command group.
 * @author Collin Heavner
 * @version 1.0
 * @since 1.0
 */
public class TestGroup extends CommandGroup {
  /**
   * Create this test group.
   */
  public TestGroup() {
    addSequential(new DriveForward(14.0d, 150000l, 0.4d));
  }
}

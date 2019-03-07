/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * The robot base. Currently, this handles the following:
 * <ul>
 *  <li>The pneumatics compressor</li>
 *  <li>The bot lift mechanism</li>
 * </ul>
 * 
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class Base extends Subsystem {

  /* The compressor */
  private final Compressor airCompressor = new Compressor(RobotMap.PNEUMATIC_CONTROL_MODULE);

  /* The solenoid that will lift the bot. */
  private final Solenoid botLift = new Solenoid(RobotMap.PNEUMATIC_CONTROL_MODULE, RobotMap.LIFT_MECHANISM);

  public Base() {
    /*
     * Enable closed loop control on the air compressor.
     * This will automatically take care of turning the compressor on and
     * off.
     */
    airCompressor.setClosedLoopControl(true);
  }


  /**
   * Set the state of the lift mechanism.
   * @param liftBot The state of the lift. Set to true for the out position, 
   * and false for the in positon.
   */
  public void liftBot(boolean liftBot) {
    botLift.set(liftBot);
  }

  /**
   * Get the current position of the robot.
   * @return true if it is up, false if it is down.
   */
  public boolean isLifted() {
    return botLift.get();
  }

  /**
   * This subsystem covers many functions, so no default command
   * is added.
   */
  @Override
  protected void initDefaultCommand() {}
}

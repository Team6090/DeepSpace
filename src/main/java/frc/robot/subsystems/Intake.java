/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.IntakeWithJoystick;

public class Intake extends Subsystem {

  private final Compressor airCompressor = new Compressor(RobotMap.pneumaticModule);
  private final Solenoid pivotDown = new Solenoid(RobotMap.pneumaticModule, RobotMap.intakeArmPivotDown);
  private final Solenoid pivotUp = new Solenoid(RobotMap.pneumaticModule, RobotMap.intakeArmPivotUp);

  private final Victor intakeMotor = new Victor(RobotMap.intakeMotor);

  private boolean armUp;
  public Intake() {
    airCompressor.setClosedLoopControl(true);
    armUp();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new IntakeWithJoystick());
  }

  /**
   * Set the state of the intake arm pivot.
   * @param state {@code true} to put the arm down, {@code false} to bring it up.
   */
  private void setIntakeArmPivot(boolean state) {
    pivotDown.set(state);
    pivotUp.set(!state);
    armUp = !state;
  }

  /**
   * Put the intake arm up.
   */
  public void armUp() {
    setIntakeArmPivot(false);
  }

  /**
   * put the intake arm down.
   */
  public void armDown() {
    setIntakeArmPivot(true);
  }

  /**
   * Whether or not the arm is up or down.
   * @return True if the arm is up.
   */
  public boolean armIsUp() {
    return armUp;
  }

  public void setSpeed(double speed) {
    intakeMotor.set(speed);
  }

  public void stop() {
    setSpeed(0.0);
  }
}

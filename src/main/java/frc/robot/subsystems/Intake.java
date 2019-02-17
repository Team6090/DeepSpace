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
import frc.robot.commands.joystick.IntakeWithJoystick;

/**
 * Handles all of our intaking mechanisms, which includes:
 * - The air compressor (for handling pneumatics)
 * - Arm pivot solenoids
 * - An intake motor
 * - The spring-loaded hatch release solenoid.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class Intake extends Subsystem {

  /* The compressor */
  private final Compressor airCompressor = new Compressor(RobotMap.PNEUMATIC_CONTROL_MODULE);

  /* Pivot solenoids */
  private final Solenoid pivotDown = new Solenoid(RobotMap.PNEUMATIC_CONTROL_MODULE, RobotMap.INTAKE_ARM_PIVOT_DOWN);
  private final Solenoid pivotUp = new Solenoid(RobotMap.PNEUMATIC_CONTROL_MODULE, RobotMap.INTAKE_ARM_PIVOT_UP);

  private final Solenoid hatchRelease = new Solenoid(RobotMap.PNEUMATIC_CONTROL_MODULE, RobotMap.HATCH_RELEASE);

  /* Intake Motor */
  private final Victor intakeMotor = new Victor(RobotMap.INTAKE_MOTOR);

  /* Store the state of the arm. */
  private boolean armUp;

  /**
   * Set up the compressor, and put the intake arm up.
   */
  public Intake() {
    airCompressor.setClosedLoopControl(true);
    armUp();
  }

  /**
   * By default, enable joystick control.
   */
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

  /**
   * Set the speed of the intake motor.
   * @param speed The desired speed of the intake motor.
   */
  public void setSpeed(double speed) {
    intakeMotor.set(speed);
  }

  /**
   * Get the current speed of the intake motor.
   * @return The current speed of the intake motor.
   */
  public double getSpeed() {
    return intakeMotor.get();
  }

  public void hatchRelease() {
    hatchRelease.set(true);
  }

  /**
   * Stop the intake motor.
   */
  public void stop() {
    setSpeed(0.0);
  }
}

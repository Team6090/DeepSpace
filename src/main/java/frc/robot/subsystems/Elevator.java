/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.ElevatorWithJoystick;

/**
 * The elevator is a single motor. This subsystem simply handles setting the speed,
 * and collecting information such as the speed and encoder position.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class Elevator extends Subsystem {

  /* Set up the brushless elevator motor. */
  private final CANSparkMax elevatorMotor = new CANSparkMax(RobotMap.elevatorMotor, MotorType.kBrushless);

  /**
   * Get the encoder position.
   * @return The encoder position of the elevator motor.
   */
  public double getPosition() {
    return elevatorMotor.getEncoder().getPosition();
  }

  /**
   * Get the speed.
   * @return The speed of the elevator motor.
   */
  public double getSpeed() {
    return elevatorMotor.get();
  }

  /**
   * Set the speed and direction of the elevator motor.
   * @param speed The desired speed and direction of the motor.
   * Negative values will drive the motor down, positive values
   * will drive it up.
   */
  public void setSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  /**
   * Stop the elevator motor.
   */
  public void stop() {
    setSpeed(0.0);
  }

  /**
   * Get the PID controller for the elevator motor.
   * @return An instance of CANPIDController.
   */
  public CANPIDController getPIDController() {
    return elevatorMotor.getPIDController();
  }

  /**
   * Set the controller reference value based on the selected
   * control mode.
   * @param gain The value to set depending on the control mode.
   * For basic duty cycle control this should be a value between
   * -1 and 1 Otherwise: Voltage Control: Voltage (volts) Velocity
   * Control: Velocity (RPM) Position Control: Position
   * (Rotations) Current Control: Current (Amps)
   * @param ctrl The control type.
   * @return CANError Set to REV_OK if successful.
   */
  public CANError setReference(double gain, ControlType ctrl) {
    return getPIDController().setReference(gain, ctrl);
  }

  /**
   * Set the controller reference value based on the selected
   * control mode.
   * @param value The value to set depending on the control mode. For basic duty cycle control this should be a value between -1 and 1 Otherwise: Voltage Control: Voltage (volts) Velocity Control: Velocity (RPM) Position Control: Position (Rotations) Current Control: Current (Amps)
   * @param ctrl The control type to override with.
   * @param pidSlot The PID slot to run this command in.
   * @return CANError Set to REV_OK if successful
   */
  public CANError setReference(double value, ControlType ctrl, int pidSlot) {
    return getPIDController().setReference(value, ctrl, pidSlot);
  }

  /**
   * By default, enable control with the joystick.
   */
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorWithJoystick());
  }
}

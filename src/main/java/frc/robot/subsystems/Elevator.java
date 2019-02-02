/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.ElevatorWithJoystick;

public class Elevator extends Subsystem {
  private final CANSparkMax elevatorMotor = new CANSparkMax(RobotMap.elevatorMotor, MotorType.kBrushless);

  public double getPosition() {
    return elevatorMotor.getEncoder().getPosition();
  }

  public double getSpeed() {
    return elevatorMotor.get();
  }

  public void setSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  public void stop() {
    setSpeed(0.0);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorWithJoystick());
  }
}

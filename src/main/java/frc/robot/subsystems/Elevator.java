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

public class Elevator extends Subsystem {
  private final CANSparkMax elevatorMotor = new CANSparkMax(RobotMap.elevatorMotor, MotorType.kBrushless);

  public double getElevatorMotorPosition() {
    return elevatorMotor.getEncoder().getPosition();
  }

  public double getElevatorSpeed() {
    return elevatorMotor.get();
  }

  public void setElevatorSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  public void stopElevator() {
    setElevatorSpeed(0.0);
  }

  @Override
  public void initDefaultCommand() {
    
  }
}

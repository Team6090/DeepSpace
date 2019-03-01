/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

/**
 * A simple class to output a lot of debug values to the SmartDashboard.
 * 
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class RobotDebug {

    /* Whether or not to output debug information to the network tables. */
    private boolean showOutput;

    /* The elevator motor reference, used to output more statistics than offered by the Elevator subsystem. */
    private WPI_TalonSRX elevatorMotor = Robot.elevator.getMotor();

    /**
     * Construct the debugger class. This is for controlling whether or not we
     * actually output anything.
     * @param showOutput Whether or not do actually do anything.
     */
    public RobotDebug(boolean showOutput) {
        this.showOutput = showOutput;
    }

    /**
     * Update the SmartDashboard values, but only if the showOutput flag is set.
     * Call this in {@code Robot.robotPeriodic}.
     */
    public void update() {
        if (showOutput) {
            /* Drivetrain speeds */
            SmartDashboard.putNumber("DriveTrain_SpeedLeft", Robot.drivetrain.getLeft());
            SmartDashboard.putNumber("DriveTrai_nSpeedRight", Robot.drivetrain.getRight());

            /* Drivetrain encoders */
            SmartDashboard.putNumber("DriveTrain_EncoderLeft", Robot.drivetrain.getLeftEncoderPosition());
            SmartDashboard.putNumber("DriveTrain_EncoderRight", Robot.drivetrain.getRightEncoderPosition());

            /* Gyro readings */
            SmartDashboard.putNumber("Gyro_Yaw", Robot.drivetrain.getGyroYaw());
            SmartDashboard.putNumber("Gyro_Pitch", Robot.drivetrain.getGyroPitch());
            SmartDashboard.putNumber("Gyro_Roll", Robot.drivetrain.getGyroRoll());
            SmartDashboard.putNumber("Gyro_CompassHeading", Robot.drivetrain.getGyroCompassHeading());

            /* Elevator */
            SmartDashboard.putNumber("Elevator_Speed", Robot.elevator.getSpeed());
            SmartDashboard.putNumber("Elevator_Encoder", Robot.elevator.getPosition());
            /* Output every possible relevant statistic on the elevator motor. */
            SmartDashboard.putNumber("Elevator_BusVoltage", elevatorMotor.getBusVoltage());
            SmartDashboard.putNumber("Elevator_OutputPercent", elevatorMotor.getMotorOutputPercent());
            SmartDashboard.putNumber("Elevator_OutputVoltage", elevatorMotor.getMotorOutputVoltage());
            SmartDashboard.putNumber("Elevator_OutputCurrent", elevatorMotor.getOutputCurrent());
            SmartDashboard.putNumber("Elevator_Temperature", elevatorMotor.getTemperature());
            SmartDashboard.putBoolean("Elevator_IsInverted", elevatorMotor.getInverted());
    
            /* Intake motor speed and arm status */
            SmartDashboard.putNumber("Intake_Speed", Robot.intake.getSpeed());
            SmartDashboard.putBoolean("Intake_ArmIsUp", Robot.intake.armIsUp());
        }
    }
}

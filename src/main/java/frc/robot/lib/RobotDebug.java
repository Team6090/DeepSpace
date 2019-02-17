/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib;

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
            SmartDashboard.putNumber("SpeedLeft", Robot.drivetrain.getLeft());
            SmartDashboard.putNumber("SpeedRight", Robot.drivetrain.getRight());

            /* Drivetrain encoders */
            SmartDashboard.putNumber("EncoderLeft", Robot.drivetrain.getLeftEncoderPosition());
            SmartDashboard.putNumber("EncoderRight", Robot.drivetrain.getRightEncoderPosition());

            /* Gyro readings */
            SmartDashboard.putNumber("GyroYaw", Robot.drivetrain.getGyroYaw());
            SmartDashboard.putNumber("GyroPitch", Robot.drivetrain.getGyroPitch());
            SmartDashboard.putNumber("GyroRoll", Robot.drivetrain.getGyroRoll());
            SmartDashboard.putNumber("GyroCompassHeading", Robot.drivetrain.getGyroCompassHeading());

            /* Elevator speed and encoder */
            SmartDashboard.putNumber("SpeedElevator", Robot.elevator.getSpeed());
            SmartDashboard.putNumber("EncoderElevator", Robot.elevator.getPosition());
    
            /* Intake motor speed and arm status */
            SmartDashboard.putNumber("SpeedIntake", Robot.intake.getSpeed());
            SmartDashboard.putBoolean("IntakeArmIsUp", Robot.intake.armIsUp());
        }
    }
}

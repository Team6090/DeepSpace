/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
   /* Drivetrain */
   public static final int leftMotor = 1;
   public static final int leftSlaveMotor = 2;
   public static final int rightMotor = 3;
   public static final int rightSlaveMotor = 4;

   /* Elevator */
   public static final int elevatorMotor = 5;

   /* Intake */
   public static final int intakeArmPivotDown = 4;
   public static final int intakeArmPivotUp = 5;
   public static final int intakeMotor = 1;
   public static final int hatchRelease = 7;

   /* USB Cameras */
   public static final int frontCamera = 0;
   public static final int rearCamera  = 1;

   /* Misc. */
   public static final int pneumaticModule = 60;
}

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

   /*
    * CAN Motors 
    */

   /* Drivetrain */
   public static final int LEFT_MOTOR = 1;
   public static final int LEFT_SLAVE_MOTOR = 2;
   public static final int RIGHT_MOTOR = 3;
   public static final int RIGHT_SLAVE_MOTOR = 4;

   /* Elevator */
   public static final int ELEVATOR_MOTOR = 5;

   /* Intake */
   public static final int INTAKE_MOTOR = 6;

   /*
    * Pneumatics
    */

   public static final int PNEUMATIC_CONTROL_MODULE = 60;

   public static final int INTAKE_ARM_PIVOT_DOWN = 4;
   public static final int INTAKE_ARM_PIVOT_UP = 5;
   public static final int HAB_2_LIFT = 6;
   public static final int HATCH_RELEASE = 7;

   public static final int LIFT_MECHANISM = 2;
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Control the elevator with the joystick.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class ElevatorController extends Command {

  /* A small deadband on the elevator joystick */
  private final double joystickDeadband = 0.2d;

  /* The maximum height the elevator can travel */
  private final int maxHeight = 9999999;
  /* The base increment for postion control */
  private final int increment = 50;

  /*
   * Position references for elevator setpoints
   */
  private final int bottomHatchRef = 10;
  private final int middleHatchRef = 2000;
  private final int topHatchRef = 4000;

  /*
   * Loop variables, used in setting the position reference
   * on the motor.
   */
  private double manualOffset = 0;
  private int presetPosition = 0;
  private double positionRef = 0;
  private double basePosition = 0;

  /**
   * Construct the ElevatorWithJoystick command. This command requires
   * the elevator subystem.
   */
  public ElevatorController() {
    requires(Robot.elevator);
  }

  @Override
  protected void initialize() {
    manualOffset = Robot.elevator.getPosition();
  }

  /**
   * Use the XBox Controller to control the elevator. 
   * 
   * Button mapping:
   * <pre>
   * A -> Elevator bottom
   * B -> Elevator middle
   * C -> Elevator top
   * Y -> Manual control using joystick.
   * </pre>
   */
  @Override
  protected void execute() {
    /* The speed reference is pulled directly from the joystick. */
    double speedRef = Robot.oi.xBoxLeftJoystickVertical();
    /* The XBox controller has a small amount of drift, so do nothing if it's not within the deadband. */
    if ((!(speedRef > joystickDeadband) && !(speedRef < -joystickDeadband))) {
      speedRef = 0.0d;
    }
    /* Get the feedback. */
    presetPosition = Robot.elevator.getPosition();

    /*
     * If the Y-button is pressed, control the elevator by speed reference.
     * By default, control the elevator by position reference.
     */
    if (Robot.oi.xBoxY()) {
      Robot.elevator.setSpeed(speedRef);
      manualOffset = 0;
      basePosition = presetPosition;
    } else {
      if (Robot.oi.xBoxA()) {
        basePosition = bottomHatchRef;
      } else if (Robot.oi.xBoxX()) {
        basePosition = middleHatchRef;
      } else if (Robot.oi.xBoxB()) {
        basePosition = topHatchRef;
      }
      /* Calculate the manual offset */
      if (((manualOffset + presetPosition) < maxHeight)) {
        manualOffset = manualOffset + (-increment * speedRef);
      }
      /* Calculate the position reference */
      positionRef = basePosition + manualOffset;
      /* Use MotionMagic to get to the position reference */
      Robot.elevator.set(ControlMode.MotionMagic, positionRef);
    }

    /* Print out some stuff - Uncomment to view. */
    //System.out.println("manualOffset = " + manualOffset + " presetPosition = " + presetPosition + " positionRef = " + positionRef + " speedRef = " + speedRef);
  }

  /**
   * This command will run until it is interrupted.
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Stop the elevator.
   */
  @Override
  protected void end() {
    Robot.elevator.stop();
  }

  /**
   * End this command.
   */
  @Override
  protected void interrupted() {
    end();
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Control the elevator with the joystick.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class ElevatorController extends Command {

  /* The maximum height the elevator can travel */
  private final double maxHeight = 9999;
  /* The base increment for postion control */
  private final double increment = 2;

  /*
   * Position references for elevator setpoints
   */
  private final double bottomRef = 1;
  private final double middleHatchRef = 5;
  private final double topHatchRef = 10;

  /*
   * Loop variables, used in setting the position reference
   * on the motor.
   */
  private double manualOffset = 0;
  private double presetPosition = 0;

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
    presetPosition = Robot.elevator.getPosition();

    if (Robot.oi.xBoxA()) {
      manualOffset = 0;
      presetPosition = bottomRef;
    } else if (Robot.oi.xBoxB()) {
      manualOffset = 0;
      presetPosition = middleHatchRef;
    } else if (Robot.oi.xBoxX()) {
      manualOffset = 0;
      presetPosition = topHatchRef;
    }
    double ref = 0;
    if (Robot.oi.xBoxY()) {
      if (((manualOffset + presetPosition) < maxHeight)) {
        manualOffset = manualOffset + (-increment * speedRef);
      }
      ref = manualOffset - presetPosition;
      Robot.elevator.setReference(ref, ControlType.kPosition, 1);
    } else {
      manualOffset = presetPosition;
    }

    System.out.println("manualOffset = " + manualOffset + " Preset Position = " + presetPosition + " Ref = " + ref);
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

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
  private final double maxHeight = 12;
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

    /*
     * The different setpoints. See the button mapping chart above,
     * this is all pretty straight-forward. Each block sets the manual
     * offset to 0, and sets the preset postition to the desired position
     * depending on which button was pressed.
     */
    if (Robot.oi.xBoxA()) {
      manualOffset = 0;
      presetPosition = bottomRef;
    } else if (Robot.oi.xBoxB()) {
      manualOffset = 0;
      presetPosition = middleHatchRef;
    } else if (Robot.oi.xBoxX()) {
      manualOffset = 0;
      presetPosition = topHatchRef;
    } else {
      /* No buttons were pressed, calculate the manual offset */
      if (((manualOffset + presetPosition) < maxHeight) || (speedRef > 0)) {
        manualOffset = manualOffset + (increment * speedRef);
      }
    }

    /* The Y button allows manual control over the elevator. */
    if (Robot.oi.xBoxY()) {
      /* 
       * Pass the joystick reference into the velocity controller.
       * 
       * I could not find a "percent control" which is what we used for the
       * TalonSRX last year, but according to my research (which may not be correct),
       * this should achieve the same effect.
       */
      Robot.elevator.setReference(speedRef, ControlType.kVelocity);
    } else {
      /* The absolute position to end at. */
      double targetAbsolutePosition = presetPosition + manualOffset;
      /*
       * Pass the target position into the position controller.
       * 
       * Not quite MotionMagic though, so it probably won't have all the
       * fancy ramp-up and ramp-down stuff, but it should get the job done.
       * 
       * Theoretically. Maybe. I'll be honest I'm not quite sure exactly what
       * this will do, because I have no experience with the CANSparkMax API.
       */
      Robot.elevator.setReference(targetAbsolutePosition, ControlType.kPosition);
    }
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

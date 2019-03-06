/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

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

  /* The maximum and minimum height the elevator can travel */
  private final int maxHeight = 25290;
  private final int minHeight = 0;
  /*
   * The base increment for postion control. This controls the speed of the elevator
   * in the position loop.
   */
  private final int increment = 170;

  /*
   * Position references for elevator setpoints
   */
  private final int bottomBallRef = 7500, middleBallRef = 16390, topBallRef = 25070;
  private final int bottomHatchRef = 0, middleHatchRef = 8727, topHatchRef = 17500;

  /*
   * Loop variables, used in setting the position reference
   * on the motor.
   */
  private double manualOffset = 0.0d;
  private double positionRef = 0.0d;
  private double basePosition = 0.0d;

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
    /* 
     * The speed reference is pulled directly from the joystick.
     * This is multiplied by -1 because on the joystick, up is down, and
     * down is up. So by multiplying by -1, it flips this so that up on the
     * joystick is up on the elevator.
     */
    double speedRef = -1.0 * Robot.oi.xBoxLeftJoystickVertical();

    /* The XBox controller has a small amount of drift, so do nothing if it's not within the deadband. */
    if ((speedRef < joystickDeadband) && (speedRef > -joystickDeadband)) {
      speedRef = 0.0d;
    }

    /* If joystick button 7 is pressed, reset the elevator's encoder to zero. */
    if (Robot.oi.getJoystickButton(7)) {
      Robot.elevator.zeroPosition();
    }

    /* Get the current position of the elevator */
    double currentPosition = Robot.elevator.getPosition();

    /*
     * If the Y-button is pressed, control the elevator by speed reference.<br>
     * This passes the raw joystick axis value directly into the motor, which
     * makes sense because:
     * <ul>
     *  <li>The motor scale goes from -1.0 to 1.0</li>
     *  <li>The joystick scale goes from -1.0 to 1.0</li>
     * </ul>
     * <br><br>
     * By default, the elevator is controlled by a position reference.
     */
    if (Robot.oi.xBoxY()) {
      /* Set the elevator motor speed to the reference from the joystick */
      Robot.elevator.setSpeed(speedRef);
      /* Set the position offset to zero, because we are not using the position loop */
      manualOffset = 0.0d;
      /*
       * Set the base position to the current position.
       * This is done because when we switch back to the position
       * loop, we want to ensure the elevator stays where it is when
       * it left the speed control loop.
       */
      basePosition = currentPosition;
    } else {
      /*
       * These are our setpoints. When the respective button is pressed, the setpoint is
       * assigned to the base position.
       * 
       * The intake arm determines which reference to set.
       * So:
       * Intake arm down -> Use ball reference.
       * Intake arm up   -> Use hatch reference.
       */
      if (Robot.oi.xBoxA()) {
        /* When the A button is pressed, set the base position to the bottom reference. */
        if (Robot.intake.armIsUp()) {
          basePosition = bottomHatchRef;
        } else {
          basePosition = bottomBallRef;
        }
      } else if (Robot.oi.xBoxX()) {
        /* When then X button is pressed, set the base position to the middle reference. */
        if (Robot.intake.armIsUp()) {
          basePosition = middleHatchRef;
        } else {
          basePosition = middleBallRef;
        }
      } else if (Robot.oi.xBoxB()) {
        /* When the B button is pressed, set the base position to the top reference. */
        if (Robot.intake.armIsUp()) {
          basePosition = topHatchRef;
        } else {
          basePosition = topBallRef;
        }

      }

      /*
       * TODO: Document this block! 
       */
      manualOffset = manualOffset + (increment * speedRef);
      if ((manualOffset + basePosition) < minHeight) {
        manualOffset = (minHeight - basePosition);
      }
      if ((manualOffset + basePosition) > maxHeight) {
        manualOffset = (maxHeight - basePosition);
      }

      /* TODO: Document this! */
      positionRef = basePosition + manualOffset;

      /* Use MotionMagic to set the position reference */
      Robot.elevator.set(ControlMode.MotionMagic, positionRef);
    }

    Robot.debug.put("Elevator_BasePosition", basePosition);
    Robot.debug.put("Elevator_ManualOffset", manualOffset);
    Robot.debug.put("Elevator_PositonRef", positionRef);
    Robot.debug.put("Elevator_Error", positionRef - currentPosition);
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

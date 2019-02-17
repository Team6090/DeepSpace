/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commandgroups.TestGroup;
import frc.robot.commands.CommandDestroyer;
import frc.robot.commands.vision.GaffTapeAlign;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class OI {
  /* Joysticks Ports */
  public static final int JOYSTICK_PORT = 0;
  public static final int XBOX_PORT = 1;

  /* Joystick Axes & Buttons */
  public static final int SLIDER_AXIS = 3;
  public static final int JOYSTICK_BUTTON_COUNT = 12;

  /* Throttle configurations */
  public static final double THROTTLE_LOWER_BOUND = 0.3d;


  /* Joysticks */
  private Joystick joystick = new Joystick(JOYSTICK_PORT);
  private Joystick xBoxJoystick = new Joystick(XBOX_PORT);

  /* Joystick Buttons */
  private JoystickButton[] joystickButton = new JoystickButton[JOYSTICK_BUTTON_COUNT + 1];
  
  public OI() {
    /* Instantiate all the buttons for easy use, and less code. */
    joystickButton[0] = null; /* There is no button on 0. */
    for (int i = 1; i <= JOYSTICK_BUTTON_COUNT; i++) {
      joystickButton[i] = new JoystickButton(joystick, i);
    }
    /* Assign button actions here */
    joystickButton[11].whenPressed(new CommandDestroyer());
    /* TODO: These are temporary, remove for comp. */
    joystickButton[3].whenPressed(new GaffTapeAlign(0.2, 1.5, -2, 2));
    joystickButton[4].whenPressed(new TestGroup());
  }

  /**
   * Get the raw value of the slider axis, and call it throttle.
   * @return The raw axis value of the slider.
   */
  public double getRawThrottle() {
    return this.joystick.getRawAxis(SLIDER_AXIS);
  }

  /**
   * Get the adjusted throttle scale from the raw throttle.
   * This translation currently takes the scale: -1 -> 0 -> 1
   * <br>
   * And converts it to the scale:              0 -> 0.5 -> 1
   * <br>
   * Then it applies the scale to go from the lower bound -> 1
   * @return The throttle percent, set by the slider. 
   */
  public double getThrottle() {
    /* Set the scale to go 0 -> 1 */
    double throttle = (0.5d * getRawThrottle()) + 0.5d;
    /* Calculate the scale to go from THROTTLE_LOWER_BOUND -> 1 */
    return ((1 - THROTTLE_LOWER_BOUND) * throttle) + THROTTLE_LOWER_BOUND;
  }

  /**
   * Apply the slider throttle to the X axis.
   * @return The throttle, multiplied by the raw X axis.
   */
  public double getThrottledX() {
    return getThrottle() * joystick.getX();
  }

  /**
   * Apply the slider throttle to the Y axis.
   * @return The throttle, multiplied by the raw Y axis.
   */
  public double getThrottledY() {
    return getThrottle() * joystick.getY();
  }

  /**
   * Apply the slider throttle to the Z axis.
   * @return The throttle, multiplied by the raw Z axis.
   */
  public double getThrottledZ() {
    return getThrottle() * joystick.getZ();
  }
  /**
   * Get the state of the specifed button on the regular joystick.
   * @param button The button to retrieve. They are labled on our joystick.
   * @return Whether or not the specified button is pressed.
   */
  public boolean getJoystickButton(int button) {
    return this.joystick.getRawButton(button);
  }

  /**
   * All possible states of the Joystick POV.
   */
  public static enum JoystickPovPosition {
    UP, DOWN, LEFT, RIGHT,
    DIAG_UP_LEFT, DIAG_UP_RIGHT, DIAG_DOWN_LEFT, DIAG_DOWN_RIGHT,
    NEUTRAL, UNKNOWN
  }

  /**
   * Get the state of the Joystick POV.
   * @return A JoystickPovPosition that represents the current POV position.
   */
  public JoystickPovPosition getJoystickPOV() {
    switch(this.joystick.getPOV()) {
      case -1:
        return JoystickPovPosition.NEUTRAL;
      case 0:
        return JoystickPovPosition.UP;
      case 45:
        return JoystickPovPosition.DIAG_UP_RIGHT;
      case 90:
        return JoystickPovPosition.RIGHT;
      case 135:
        return JoystickPovPosition.DIAG_DOWN_RIGHT;
      case 180:
        return JoystickPovPosition.DOWN;
      case 225:
        return JoystickPovPosition.DIAG_DOWN_LEFT;
      case 270:
        return JoystickPovPosition.LEFT;
      case 315:
        return JoystickPovPosition.DIAG_UP_LEFT;
      default:
        return JoystickPovPosition.UNKNOWN;
      
    }
  }

  /**
   * Get the state of the A button on the XBox controller.
   * @return Whether or not the A button is pressed.
   */
  public boolean xBoxA() {
    return this.xBoxJoystick.getRawButton(1);
  }

  /**
   * Get the state of the B button on the XBox controller.
   * @return Whether or not the B button is pressed.
   */
  public boolean xBoxB() {
    return this.xBoxJoystick.getRawButton(2);
  }

  /**
   * Get the state of the X button on the XBox controller.
   * @return Whether or not the X button is pressed.
   */
  public boolean xBoxX() {
    return this.xBoxJoystick.getRawButton(3);
  }

  /**
   * Get the state of the Y button on the XBox controller.
   * @return Whether or not the Y button is pressed.
   */
  public boolean xBoxY() {
    return this.xBoxJoystick.getRawButton(4);
  }

  /**
   * Get the state of the left bumper on the XBox controller.
   * @return Whether or not the left bumper is pressed.
   */
  public boolean xBoxLeftBumper() {
    return this.xBoxJoystick.getRawButton(5);
  }

  /**
   * Get the state of the right bumper on the XBox controller.
   * @return Whether or not the right bumper is pressed.
   */
  public boolean xBoxRightBumper() {
    return this.xBoxJoystick.getRawButton(6);
  }

  /**
   * Get the left trigger raw value on the XBox controller.
   * @return The current position of the left trigger.
   */
  public double xBoxLeftTrigger() {
    return this.xBoxJoystick.getRawAxis(2);
  }

  /**
   * Get the right trigger raw value on the XBox controller.
   * @return The current position of the right trigger.
   */
  public double xBoxRightTrigger() {
    return this.xBoxJoystick.getRawAxis(3);
  }

  /**
   * Get the horizontal raw value of the left joystick on the
   * XBox controller.
   * @return The current position of the left joystick in the
   * horizontal direction.
   */
  public double xBoxLeftJoystickHorizontal() {
    return this.xBoxJoystick.getRawAxis(0);
  }

  /**
   * Get the vertical raw value of the left joystick on the
   * XBox controller.
   * @return The current position of the left joystick in the
   * vertical direction.
   */
  public double xBoxLeftJoystickVertical() {
    return this.xBoxJoystick.getRawAxis(1);
  }

  /**
   * Get the horizontal raw value of the right joystick on the
   * XBox controller.
   * @return The current position of the right joystick in the
   * horizontal direction.
   */
  public double xBoxRightJoystickHorizontal() {
    return this.xBoxJoystick.getRawAxis(4);
  }

  /**
   * Get the vertical raw value of the right joystick on the
   * XBox controller.
   * @return The current position of the right joystick in the
   * vertical direction.
   */
  public double xBoxRightJoystickVertical() {
    return this.xBoxJoystick.getRawAxis(5);
  }

  /**
   * All possible states of the XBox POV.
   */
  public static enum XBoxPovPosition {
    UP, DOWN, LEFT, RIGHT,
    NEUTRAL, UNKNOWN
  }

  /**
   * The position of the XBox POV.
   * <br>
   * The {@code getPOV()} function returns -1 when neutral, 0 if up,
   * 90 if right, 180 if bottom, 270 if left.
   * @return An XBoxPovPosition that reflects the position of the XBoxPov.
   */
  public XBoxPovPosition xBoxPOV() {
    //return this.xBoxJoystick.getPOV();
    switch(this.xBoxJoystick.getPOV()) {
      case -1:
        return XBoxPovPosition.NEUTRAL;
      case 0:
        return XBoxPovPosition.UP;
      case 90:
        return XBoxPovPosition.RIGHT;
      case 180:
        return XBoxPovPosition.DOWN;
      case 270:
        return XBoxPovPosition.LEFT;
      default:
        return XBoxPovPosition.UNKNOWN;
      
    }
  }

  /**
   * Calculate a deadband mod.
   * 
   * @param joystickInput The input on the joystick to mod
   * @param deadband The deadband to apply to the {@code joystickInput}
   * @return The result of the mod.
   */
  public double deadbandMod(double joystickInput, double deadband) {
    /* This will be our result */
    double mod;
    /* Compute the deadband mod */
    if (joystickInput < 0.0d) {
        if (joystickInput <= -deadband) {
            mod = joystickInput + deadband;
        } else {
            mod = 0.0d;
        }
    } else {
        if (joystickInput >= deadband) {
            mod = joystickInput - deadband;
        } else {
            mod = 0.0d;
        }
    }
    /* Return the result. */
    return mod;
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.joystick.ElevatorController;

/**
 * The elevator is a single motor. This subsystem simply handles setting the speed,
 * and collecting information such as the speed and encoder position.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class Elevator extends Subsystem {

  /* Set up the elevator motor. */
  private final WPI_TalonSRX elevatorMotor = new WPI_TalonSRX(RobotMap.elevatorMotor);
  
  private final int timeoutMs = 10;

  /**
   * Set up the elevator motor and subsystem.
   */
  public Elevator() {
    /*
		 * Setup the closed loop parameters for the winch Talon. For the
		 * SelectProfileSlot: First parameter is setpoint slot (0-4) Second parameter is
		 * PID loop number - usually 0 for primary loop
		 * 
		 * kFeed forward=(Percent motor load to achieve max speed) * 1023/max spd in
		 * encoder cnts/100ms To set-run full speed in manual joystick mode and read
		 * talon speed feedback and output percent from the Self-Test web interface
		 * roborio-6090-frc.local
     * 
     * Slot 0 is wench coarse movement UP and DOWN during Teleop (Slot 0, PID loop number=0)
		 */
    elevatorMotor.selectProfileSlot(0, 0);
  
    /* Feed Forward gain - PID loop num, value, timout */
    elevatorMotor.config_kF(0, .40, timeoutMs);
    /* Proportional gain - PID loop num, value, timout */
    elevatorMotor.config_kP(0, 0.3, timeoutMs);
    /* Integral gain - PID loop num, value, timout */
    elevatorMotor.config_kI(0, 0.0001, timeoutMs);
    /* Differential gain - PID loop num, value, timout */
    elevatorMotor.config_kD(0, 0, timeoutMs);
    /* This doesn't appear to do anything useful */
    elevatorMotor.configAllowableClosedloopError(0, 50, timeoutMs);
    /* Enable integral component when error gets below this threshold */
    elevatorMotor.config_IntegralZone(0, 5, timeoutMs);
    /* Number of msec to allow peak current */
    elevatorMotor.configPeakCurrentDuration(50, timeoutMs);
    /* Continuous allow current in Amps */
    elevatorMotor.configContinuousCurrentLimit(80, timeoutMs);
    /* Peak current in Amps */
    elevatorMotor.configPeakCurrentLimit(68, timeoutMs);
    /* If error is below threshold, consider to be "in position" (0.25=25%) */
    elevatorMotor.configNeutralDeadband(0.03, timeoutMs);
    
    /*
		 * Set accel and cruise velocity to use to make trapezoid profile Cruise speed =
		 * 95% of max speed = 3800*0.95 = 3610 Acc rate = Cruise spd in 0.5 sec =
		 * 3610/0.5 = 7220
     * 
     * Winch motor has a magnetic pickup encoder hooked to talon
		 */
    elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    /* Make sure talons blink green when position increasing */
    elevatorMotor.setSensorPhase(true);
    /* Invert the speed reference */
		elevatorMotor.setInverted(true);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		elevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, timeoutMs);
    elevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeoutMs);
    /* Cruize velociy in encoder/100msec */
    elevatorMotor.configMotionCruiseVelocity(3800, timeoutMs);
    /*  Accel */
    elevatorMotor.configMotionAcceleration(5500, timeoutMs);
    /* Zero the position encoder */
    elevatorMotor.setSelectedSensorPosition(0, 0, timeoutMs);
    
    /*
		 * Set Open and Close Loop ramp rate -- Higher values give slower ramp--
		 * 
		 * Rate is change in output / 10msec chunks Max Talon speed reference = 1023
		 * counts internally so speed change = end speed - start speed
     * 
     * Rate = [1023-0] / [1000 msec] * 10 msec = 10
		 */
    elevatorMotor.configOpenloopRamp(0.5, timeoutMs);
    /* Rate = [1023-0] / [500 msec] * 10 msec = 20 */
		elevatorMotor.configClosedloopRamp(0.5, timeoutMs);
  }

  /**
   * Get the encoder position.
   * @return The encoder position of the elevator motor.
   */
  public int getPosition() {
    /* Get Winch drive motor Position from Talon SRX */
    return elevatorMotor.getSelectedSensorPosition(0);
  }

  /**
   * Get the speed.
   * @return The speed of the elevator motor.
   */
  public double getSpeed() {
    return elevatorMotor.get();
  }

  /**
   * Set the speed and direction of the elevator motor.
   * @param speed The desired speed and direction of the motor.
   * Negative values will drive the motor down, positive values
   * will drive it up.
   */
  public void setSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  public void set(ControlMode mode, double reference) {
    elevatorMotor.set(mode, reference);
  }

  /**
   * Stop the elevator motor.
   */
  public void stop() {
    setSpeed(0.0);
  }

  /**
   * By default, enable control with the joystick.
   */
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorController());
  }
}

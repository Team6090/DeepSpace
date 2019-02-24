/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;

/**
 * The drivetrain is a simple tank drive, with two motors on each side.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class DriveTrain extends Subsystem {

  /* Wheel measurements in inches */
  public static final double WHEEL_DIAMETER = 7.87d;
  /* Theoretical: (WHEEL_DIAMETER * Math.PI) (Approx 24.7 inches) - Actual: 22 and 3/8 inches */
  public static final double WHEEL_CIRCUMFERENCE = 22.0d + (3.0d / 8.0d);
  /* Gearbox ratio */
  public static final double GEAR_RATIO = 14.17d;
  /*Constant: motor revs per inch*/
  public static final double ENCODER_CONSTANT = 0.603125d;

  /* 
   * The ramp rate to apply to all of the motors (in seconds)
   * TODO: Tweak this
   */
  public static final double RAMP_RATE = 0.0d;

  /* The differential drive deadband. */
  public static final double DEADBAND = 0.02d;

  /* Set up the motors. */
  private final CANSparkMax leftMotor = new CANSparkMax(RobotMap.LEFT_MOTOR, MotorType.kBrushless);
  private final CANSparkMax leftSlaveMotor = new CANSparkMax(RobotMap.LEFT_SLAVE_MOTOR, MotorType.kBrushless);
  private final CANSparkMax rightMotor = new CANSparkMax(RobotMap.RIGHT_MOTOR, MotorType.kBrushless);
  private final CANSparkMax rightSlaveMotor = new CANSparkMax(RobotMap.RIGHT_SLAVE_MOTOR, MotorType.kBrushless);

  /* Speed controller groups. */
  private final SpeedControllerGroup leftSideMotors = new SpeedControllerGroup(leftMotor, leftSlaveMotor);
  private final SpeedControllerGroup rightSideMotors = new SpeedControllerGroup(rightMotor, rightSlaveMotor);

  /* The differential drive. */
  private final DifferentialDrive diffdrive = new DifferentialDrive(leftSideMotors, rightSideMotors);

  /* Create the AHRS NavX Gyro */
  private final AHRS navxGyro = new AHRS(SPI.Port.kMXP);

  /**
   * Drivetrain init. Configure the ramp rates, zero the encoders,
   * set a small deadband on the differential drive.
   */
  public DriveTrain() {
    /*
     * Set the open and closed loop ramp rates.
     */
    leftMotor.setOpenLoopRampRate(RAMP_RATE);
    leftSlaveMotor.setOpenLoopRampRate(RAMP_RATE);
    rightMotor.setOpenLoopRampRate(RAMP_RATE);
    rightSlaveMotor.setOpenLoopRampRate(RAMP_RATE);

    leftMotor.setClosedLoopRampRate(RAMP_RATE);
    leftSlaveMotor.setClosedLoopRampRate(RAMP_RATE);
    rightMotor.setClosedLoopRampRate(RAMP_RATE);
    rightSlaveMotor.setClosedLoopRampRate(RAMP_RATE);

    /* Zero the encoders */
    leftMotor.setEncPosition(0.0d);
    leftSlaveMotor.setEncPosition(0.0d);
    rightMotor.setEncPosition(0.0d);
    rightSlaveMotor.setEncPosition(0.0d);

    /* Set the deadband. */
    diffdrive.setDeadband(DEADBAND);
  }

  /**
   * Convert a distance into encoder counts.
   * @param distance  The distance, in inches, to convert to motor revolutions.
   * @return Motor revolutions.
   */
  public static double distanceToMotorRevs(double distance) {
    return distance * (WHEEL_CIRCUMFERENCE / GEAR_RATIO);
  }

  /**
   * Convert motor revolutions into a distance.
   * @param motorRevs The number of motor revolutions to convert to distance.
   * @return Distance, in inches.
   */
  public static double motorRevsToDistance(double motorRevs) {
    return (motorRevs * GEAR_RATIO) / WHEEL_CIRCUMFERENCE;
  }

  public static double distanceToMotorRevs2(double distance) {
    return (distance * ENCODER_CONSTANT);
  }
  /**
   * By default, enable control from the joystick.
   */
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }

  /**
   * Used for driving with the joystick
   * @param y The Y value from the joystick.
   * @param z The Z value from the joystick.
   */
  public void arcadeDrive(double y, double z) {
    diffdrive.arcadeDrive(y, z);
  }

  /**
   * Drive the left motors.
   * @param speed The speed to set the left motors to.
   */
  public void setLeft(double speed) {
    leftSideMotors.set(speed);
  }

  /**
   * Drive the right side motors.
   * @param speed The speed to set the right motors to.
   */
  public void setRight(double speed) {
    rightSideMotors.set(speed);
  }

  /**
   * Set the speed of both the left and right motors.
   * @param speedLeft The desired left motor(s) speed percentage.
   * @param speedRight The desired right motor(s) speed percentange.
   */
  public void set(double speedLeft, double speedRight) {
    setLeft(speedLeft);
    setRight(speedRight);
  }

  /**
   * Get the left side motors speed percentage.
   * @return The speed of the left motors.
   */
  public double getLeft() {
    return leftSideMotors.get();
  }

  /**
   * Get the right side motors speed percentage.
   * @return The speed of the right motors.
   */
  public double getRight() {
    return rightSideMotors.get();
  }

  /**
   * The right motor position
   * @return The absolute encoder value of the right motor.
   */
  public double getRightEncoderPosition() {
    /* Get Right drive motor Position */
    return rightMotor.getEncoder().getPosition();
  }

  /**
   * The left motor position
   * @return The absolute encoder value of the left motor.
   */
  public double getLeftEncoderPosition() {
    /* Get Right drive motor Position */
    return leftMotor.getEncoder().getPosition();
  }

  /**
   * Stop all motors.
   */
  public void stop() {
    set(0.0d, 0.0d);
  }

  /**
   * Get the yaw from the NavX Gyro.
   * @return Gyro yaw.
   */
  public final float getGyroYaw() {
    return navxGyro.getYaw();
  }

  /**
   * Get the pitch from the NavX Gyro.
   * @return Gyro pitch.
   */
  public final float getGyroPitch() {
    return navxGyro.getPitch();
  }

  /**
   * Get the compass heading from the NavX Gyro.
   * @return Gyro Compass Heading.
   */
  public final float getGyroCompassHeading() {
    return navxGyro.getCompassHeading();
  }

  /**
   * Get the roll from the NavX Gyro.
   * @return Gyro roll.
   */
  public final float getGyroRoll(){     
    return navxGyro.getRoll();
  }

  /**
   * Zero the Yaw on the NavX Gyro.
   */
  public final void zeroGyroYaw() {
    navxGyro.zeroYaw();
  }

  /**
	 * syncAngle
	 *
   * @param angleSP Angle setpoint in degrees
   * @param angleFB Angle Feedback in degrees
	 * @return motor correction to be ADDED to left motor (scaled to speed ref (-1.0 to 1.0 = full min-to-full max
	 */
  public double syncAngle(double angleSP, double angleFB) {
		double syncAngleOut = 0.0d;			          	/* calc'ed output for method */
		double angleErr = 0.0d; 				            	/* calc'ed angle err (SP-FB) */
		double angleErrAdj = 0.0d; 			          	/* error adjusted to be +/- 180 */
		double angleErrMod = 0.0d; 			          	/* Angle Error modified by scale factor */
		double absAngleErrMod = 0.0d; 			        	/* Absolute Value of Angle Error modified by scale factor */
		double absSyncAngleOut = 0.0d; 			        /* Absolute value of reference to move robot */
		//double correctDirection = 1.0; 		    	/* Direction to rotate for shortest distance to setpoint */
		final double zAxisCorrectScale = 0.022d;  	/* Scale gyro rotation drift to send ref to z-axis */
		final double gyroMaxZaxisCorrect = 0.24d;  /* Maximum amount of z-axis correction due to rotation drift */
		final double minRotationThreash = 0.01d;   /* Min Z-Axis rotation threashold to try to correct for */
		final double minSpeedRef = 0.035d; 		    /* Min speed reference to move robot */
	
		angleErr = (angleSP - angleFB); 		      /* calc angle error modified value to get back on course */
	
		angleErrAdj = angleErr;
		if ((angleSP > angleFB) && (angleFB < 180.0d) && (angleSP > 180.0d))
			angleErrAdj = angleErr - 360.0d;
		if ((angleSP <= angleFB) && (angleFB >= 180.0d) && (angleSP < 180.0d))
			angleErrAdj = angleErr + 360.0d;
	
		angleErrMod = angleErrAdj * zAxisCorrectScale;      /* calc angle error modified value to get back on course */
		absAngleErrMod = Math.abs(angleErrMod);             /* Find absolute value of angle error modified by scale factor */
	
		/* Max positive correction is limited to max positive correction reference value */
		if (absAngleErrMod >= gyroMaxZaxisCorrect) { 						/* Limit max output */
			if (angleErrMod > 0.0d)													        /* Max positive reference */
				syncAngleOut = gyroMaxZaxisCorrect;	 								/* Limit angular reference to send to z-axis */
			else 																	                /* Max negative reference */
				syncAngleOut = (-1.0d * gyroMaxZaxisCorrect);				/* Limit angular reference to send to z-axis */
		
		}
		/*
		 * In between Max correction and Min correction - reference is scaled with angle
		 * error value.
		 */
		else if (absAngleErrMod < gyroMaxZaxisCorrect)  /* Error is less than max output limit */
			syncAngleOut = angleErrMod;                   /* Scale angular reference to send to z-axis */
		/* Correction is less than threshold to care about - reference is set to zero */
		else if (absAngleErrMod < minRotationThreash)   /* Error is less than max output limit*/
			syncAngleOut = 0;                             /* Scale angular reference to send to z-axis */
		absSyncAngleOut = Math.abs(syncAngleOut);       /* Absolute value of speed reference */
		if (absSyncAngleOut < minSpeedRef) {            /* Speed reference is less than value to cause movement */
			if (syncAngleOut < 0.0d)                       /* Scale negative angular reference to send to z-axis */
				syncAngleOut = (-1.0d * minSpeedRef);
			else
				syncAngleOut = minSpeedRef;                 /* Scale positive angular reference to send to z-axis */
		}
		return syncAngleOut;
  }
}
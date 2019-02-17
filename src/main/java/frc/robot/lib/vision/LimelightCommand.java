/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib.vision;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * A custom Limelight command that automatically handles Limelight functions for
 * every command that uses the LimeLight.
 * 
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class LimelightCommand extends Command {

    /**
     * When the LimelightCommand is initialized:
     * <ul>
     * <li>Turn the LED lights on.</li>
     * <li>Put the vision camera in the main window.</li>
     * </ul>
     */
    @Override
    protected void initialize() {
        super.initialize();
        Robot.limelight.setLedMode(Limelight.LedState.FORCE_ON);
        Robot.limelight.setStreamingMode(Limelight.StreamMode.PIP_MAIN);
        /*
         * Sleep 500 ms to give the Limelight time to process after
         * turning on the LEDs.
         */
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure all subclasses Override this!!!
     */
    @Override
    protected boolean isFinished() {
        System.out.println("LimelightCommand.isFinished() : Override This!");
        return true;
    }

    /**
     * When the LimelightCommand ends:
     * <ul>
     *  <li>Turn the LED lights off.</li>
     *  <li>Put the vision camera in the secondary window.</li>
     * </ul>
     */
    @Override
    protected void end() {
        super.end();
        Robot.limelight.setLedMode(Limelight.LedState.FORCE_OFF);
        Robot.limelight.setStreamingMode(Limelight.StreamMode.PIP_SECONDARY);
    }
}

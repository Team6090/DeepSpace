/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NetworkTableOutput {

    public boolean outputEnabled;

    public NetworkTableOutput(boolean outputEnabled) {
        this.outputEnabled = outputEnabled;
    }

    public void putString(String key, String value) {
        if (outputEnabled) {
            SmartDashboard.putString(key, value);
        }
    }

    public void putBoolean(String key, boolean value) {
        if (outputEnabled) {
            SmartDashboard.putBoolean(key, value);
        }
    }

    public void putNumber(String key, double value) {
        if (outputEnabled) {
            SmartDashboard.putNumber(key, value);
        }
    }
}

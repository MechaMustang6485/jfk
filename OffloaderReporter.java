package org.usfirst.frc.team6485.robot.utility;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.subsystems.Offloader;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Kyle Saburao
 */
public class OffloaderReporter {

  private static Offloader system = Robot.OFFLOADER;
  private static String state;

  public static void report() {
    SmartDashboard.putNumber("OFFLOADER PWM", system.getSpeed());
    switch (system.getState()) {
      case FREE:
        state = "FREE";
        break;
      case TAUT:
        state = "TAUT";
        break;
    }
    SmartDashboard.putString("OFFLOADER STATE", state);
    SmartDashboard.putBoolean("OFFLOADER S-LOCK", system.getStallLock());
  }

}

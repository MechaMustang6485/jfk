package org.usfirst.frc.team6485.robot.utility;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Kyle Saburao
 */
public class DriveTrainReporter {

  private static DriveTrain system = Robot.DRIVETRAIN;

  public static void report() {
    SmartDashboard.putNumber("Gyroscope Value", system.getGyro().getAngle());
    SmartDashboard.putBoolean("DriveTrain Encoder Moving", !system.getEncoder().getStopped());
    SmartDashboard.putNumber("DriveTrain Encoder Distance", system.getEncoder().getDistance());
    SmartDashboard.putNumber("DriveTrain Encoder Auto Distance",
        Robot.DRIVETRAIN.getAutonomousEncoderDistance());
  }

}

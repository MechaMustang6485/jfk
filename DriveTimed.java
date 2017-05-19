package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Executes drive train movement either back or forth for a specified time period.<br>
 * <br>
 * <b>Arguments:</b> double speed, double time<br>
 * <br>
 * 
 * @author Kyle Saburao
 */
public class DriveTimed extends Command {

  private final double kP = RobotMap.AUTODRIVE_GYROKP;
  private double mCurrentAngle, mStartTime, mCurrentRunTime, mRampPeriod, mTimeWindow, mStartSpeed,
      mTargetSpeed, mSpeedSlope, mCPT;
  private int mMotorIndex = 0;
  private boolean mAccelerated;
  private DriveTrain mDriveTrain;
  private Gyro mGyroscope;

  /**
   * Drives forward and uses the gyroscope to maintain direction.
   * 
   * @param speed The speed to drive
   * @param time The time to drive
   */
  @Deprecated
  public DriveTimed(double speed, double time) {
    requires(Robot.DRIVETRAIN);
    mDriveTrain = Robot.DRIVETRAIN;
    mGyroscope = mDriveTrain.getGyro();
    if (time < 0) {
      time = 0.0;
    }
    setTimeout(time + 1.0); // Will kill the command one second after its
    // allotted window.

    mTargetSpeed = speed;
    mTimeWindow = time;
    setInterruptible(false);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out
        .println(String.format("Driving at %.2f for %.2f seconds.", mTargetSpeed, mTimeWindow));

    mRampPeriod = RobotMap.DRIVETIMED_RAMPPERIODSECONDS;
    mStartSpeed = mDriveTrain.getMotorPWM(mMotorIndex); // Change index if needed.
    mSpeedSlope = (mTargetSpeed - mStartSpeed) / mRampPeriod;
    mStartTime = Timer.getFPGATimestamp();
    mAccelerated = false;

    mGyroscope.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    mCurrentRunTime = Timer.getFPGATimestamp() - mStartTime;
    mCurrentAngle = mGyroscope.getAngle();
    // Gyroscope measurement increases to the right, but the drive train
    // turn rate is negative the same direction.
    mCPT = mCurrentAngle * kP;
    if (mTargetSpeed < 0) {
      mCPT *= -1;
    }

    /*
     * Acceleration curving.
     */
    if (!mAccelerated) {
      double accelerationvelocitycompute = mSpeedSlope * mCurrentRunTime;
      mDriveTrain.arcadeDrive(
          (accelerationvelocitycompute > mTargetSpeed) ? mTargetSpeed : accelerationvelocitycompute,
          mCPT);
      if (mDriveTrain.getMotorPWM(mMotorIndex) == mTargetSpeed) {
        mAccelerated = true;
      }
    } else if (mTimeWindow - mCurrentRunTime <= mRampPeriod) {
      mDriveTrain.arcadeDrive(mSpeedSlope * (mTimeWindow - mCurrentRunTime), mCPT);
    } else
      mDriveTrain.arcadeDrive(mTargetSpeed, mCPT);

    SmartDashboard.putNumber("Gyroscope cPT", mCPT);

    // error = (distance - Robot.drivetrain.getRightEncoder().getDistance());
    // if (driveForwardSpeed * kP * error >= driveForwardSpeed) {
    // Robot.drivetrain.tankDrive(driveForwardSpeed, driveForwardSpeed);
    // } else {
    // Robot.drivetrain.tankDrive(driveForwardSpeed * kP * error, driveForwardSpeed * kP * error);
    // }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (timeSinceInitialized() >= mTimeWindow) || isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    mDriveTrain.stop();
    System.out.println(String.format("AutoDrive complete: %.2f PWM for %.3f seconds.", mTargetSpeed,
        mCurrentRunTime));
  }

}

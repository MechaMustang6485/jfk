package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Kyle Saburao
 */
public class DriveDistance extends Command {

  private double mDistanceTarget, mDistanceDriven, mSpeedTarget, mSpeed, mPTurn,
      mDistanceRampingSlope;
  private final double kTurnP = RobotMap.AUTODRIVE_GYROKP, kToleranceMetres = 0.075,
      kDistanceMetresRamping = RobotMap.DRIVEDISTANCE_RAMPINGDISTANCEMETRES;
  private boolean mComplete;
  private DriveTrain mDriveTrain;

  // private double mDistanceCurrent, mDistanceError, mDistanceErrorAbs;
  // private final double kDistanceMetresBeginSlow = 0.35;

  /**
   * Automatically drive forward a specific distance.
   * 
   * @param distance Metres (+ or -)
   * @param speed magnitude [0 to 0.95]
   */
  public DriveDistance(double distance, double speed) {
    requires(Robot.DRIVETRAIN);
    mDriveTrain = Robot.DRIVETRAIN;
    mDistanceTarget = distance;
    // Square root the target speed because arcade drive squares the inputs by default.
    mSpeedTarget = Math.copySign(Math.sqrt(Math.abs(speed)), distance);
    if (Math.abs(mSpeedTarget) > Math.sqrt(Math.abs(RobotMap.DRIVETRAIN_PWMLIMIT))) {
      mSpeedTarget = Math.copySign(Math.sqrt(Math.abs(RobotMap.DRIVETRAIN_PWMLIMIT)), distance);
    }
    setTimeout(10.0); // 10 seconds max.
    setInterruptible(false);

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    mComplete = false;
    mDriveTrain.stop();

    // Assume initial speed is 0.0 because the drive train was just commanded to stop.
    mDistanceRampingSlope = mSpeedTarget / Math.abs(kDistanceMetresRamping);

    if (Math.abs(mDistanceTarget) <= kToleranceMetres) {
      mComplete = true;
    }

    mDriveTrain.getEncoder().reset();
    mDriveTrain.getGyro().reset();
  }

  protected boolean secondaryCheck() {
    if (mDistanceTarget > 0.0) {
      return mDriveTrain.getEncoder().getDistance() >= mDistanceTarget;
    } else if (mDistanceTarget < 0.0) {
      return mDriveTrain.getEncoder().getDistance() <= mDistanceTarget;
    }
    return false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // This is a scalar amount
    mDistanceDriven = Math.abs(mDriveTrain.getEncoder().getDistance());

    if (Math.abs(mDistanceTarget) <= (2.0 * Math.abs(kDistanceMetresRamping)) + 0.25) {
      mSpeed = mSpeedTarget;
      if (mSpeed > Math.sqrt(0.75)) {
        mSpeed = Math.sqrt(0.75);
      }
    } else {
      if (mDistanceDriven <= Math.abs(kDistanceMetresRamping)) {
        mSpeed = mDistanceRampingSlope * mDistanceDriven;
      } else if (mDistanceDriven >= Math.abs(mDistanceTarget) - Math.abs(kDistanceMetresRamping)) {
        mSpeed = mDistanceRampingSlope * (Math.abs(mDistanceTarget) - mDistanceDriven);
      } else {
        mSpeed = mSpeedTarget;
      }
    }

    // Minimum speed limiter
    if (Math.abs(mSpeed) < Math.sqrt(RobotMap.DRIVEDISTANCE_MINIMUMALLOWABLEPWMMAGNITUDE)) {
      mSpeed = Math.copySign(Math.sqrt(RobotMap.DRIVEDISTANCE_MINIMUMALLOWABLEPWMMAGNITUDE),
          mSpeedTarget);
    }

    // Leave the turning to default to squared inputs.
    mPTurn = mDriveTrain.getGyro().getAngle() * kTurnP;

    // Check if complete
    if (mDistanceTarget > 0.0) {
      mComplete = mDriveTrain.getEncoder().getDistance() >= mDistanceTarget;
    } else if (mDistanceTarget < 0.0) {
      mComplete = mDriveTrain.getEncoder().getDistance() <= mDistanceTarget;
    }
    if (mComplete) {
      mSpeed = 0.0;
    }

    // Control the drive train and update a special reporter variable.
    mDriveTrain.arcadeDrive(mSpeed, mPTurn);
    mDriveTrain.setAutonomousEncoderDistance(Robot.DRIVETRAIN.getEncoder().getDistance());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return mComplete || secondaryCheck() || isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    mDriveTrain.stop();
  }

}

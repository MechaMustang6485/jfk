package org.usfirst.frc.team6485.robot.subsystems;

import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.RobotMap.OFFLOADER_STATE;
import org.usfirst.frc.team6485.robot.commands.OffloaderDriver;
import org.usfirst.frc.team6485.robot.utility.PowerDistributionPanelReporter;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The ball offloader subsystem.<br>
 * Negative PWM rolls the vinyl.
 * 
 * @author Kyle Saburao
 */
public class Offloader extends Subsystem {

  private double mStallStartTime;
  private boolean mStallInit, mStallLock;
  private final int kOffloaderPWMSlot = RobotMap.OFFLOADER_MOTOR;
  private final double kNormalPWMRate = RobotMap.OFFLOADER_MAXSAFEPWM,
      kStallTimeMax = RobotMap.OFFLOADER_STALLTIME;
  private Spark mMotor;

  private OFFLOADER_STATE mState;

  public Offloader() {
    mMotor = new Spark(kOffloaderPWMSlot);
    mState = OFFLOADER_STATE.FREE;
    mStallInit = false;
    mStallLock = false;
    mStallStartTime = 0.0;
    mMotor.setSafetyEnabled(false);
    mMotor.setSpeed(0.0);
  }

  /**
   * Limit the speed of an offloader PWM rate request.<br>
   * If the RoboRio thinks that the system is taut through extreme current draw, it will not allow a
   * negative PWM request.
   * 
   * @param speed
   * @return The allowed PWM rate
   */
  private double limit(double speed) {
    if (speed > kNormalPWMRate) {
      speed = kNormalPWMRate;
    } else if (speed < -kNormalPWMRate) {
      speed = -kNormalPWMRate;
    }
    if (mStallLock && speed < 0.0) {
      speed = 0.0;
    }
    return speed;
  }

  /**
   * The state machine handler.
   */
  public void updateState() {
    if (Math.abs(getCurrent()) > RobotMap.OFFLOADER_STALLCURRENT && getSpeed() < 0.0) {
      mState = OFFLOADER_STATE.TAUT;
    } else if (getSpeed() > 0.0) {
      mState = OFFLOADER_STATE.FREE;
      mStallInit = false;
      mStallLock = false;
    }
    if (mState == OFFLOADER_STATE.TAUT && !mStallInit && !mStallLock) {
      mStallStartTime = Timer.getFPGATimestamp();
      mStallInit = true;
      mStallLock = false;
    }
    if (mStallInit && !mStallLock
        && (Timer.getFPGATimestamp() - mStallStartTime >= kStallTimeMax)) {
      mStallLock = true;
    }
  }

  public double getSpeed() {
    return mMotor.getSpeed();
  }

  /**
   * @return The current of the offloader motor in amperes.
   */
  public double getCurrent() {
    return PowerDistributionPanelReporter.getChannelCurrent(RobotMap.PDP_OFFLOADER);
  }

  /**
   * Sets the motor speed of the offloader.
   * 
   * @param speed The PWM Rate
   */
  public void set(double speed) {
    mMotor.setSpeed(limit(speed));
    updateState();
  }

  /**
   * Stops the offloader motor.
   */
  public void stop() {
    mMotor.setSpeed(0.0);
    updateState();
  }

  public OFFLOADER_STATE getState() {
    updateState();
    return mState;
  }

  public double getStallTime() {
    if (mStallInit) {
      return Timer.getFPGATimestamp() - mStallStartTime;
    } else {
      return 0.0;
    }
  }

  public boolean getStallInit() {
    return mStallInit;
  }

  public boolean getStallLock() {
    return mStallLock;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new OffloaderDriver());
  }

}

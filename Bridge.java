package org.usfirst.frc.team6485.robot.subsystems;

import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.commands.BridgeDriver;
import org.usfirst.frc.team6485.robot.utility.PowerDistributionPanelReporter;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Kyle Saburao
 */
public class Bridge extends Subsystem {

  private Spark mMotor;
  private Encoder mBridgeEncoder;

  // private final double kMotorSpeed = RobotMap.BRIDGE_NORMALPWM;
  private final double kMaxSpeedMagnitude = RobotMap.BRIDGE_MAXSAFEPWM;

  public Bridge() {
    mMotor = new Spark(RobotMap.BRIDGE_MOTOR);
    mMotor.setSafetyEnabled(false);
    mMotor.setSpeed(0.0);

    mBridgeEncoder = new Encoder(3, 4);
    mBridgeEncoder.setSamplesToAverage(2);
    mBridgeEncoder.setMaxPeriod(1.0);
    mBridgeEncoder.setDistancePerPulse((1.0 / 7.0) * 71.0);
  }

  /**
   * Limits the bridge motor PWM magnitude.
   * 
   * @param speed The requested bridge motor PWM rate.
   * @return The safe bridge motor PWM rate.
   */
  private double mLimitSpeed(double speed) {
    String warning = "";

    if (speed > kMaxSpeedMagnitude) {
      warning = String.format("WARNING: Bridge speed request is %.3f higher than safe maximum.",
          speed - kMaxSpeedMagnitude);
      speed = kMaxSpeedMagnitude;
    } else if (speed < -kMaxSpeedMagnitude) {
      warning = String.format("WARNING: Bridge speed request is %.3f lower than safe maximum.",
          speed - -kMaxSpeedMagnitude);
      speed = -kMaxSpeedMagnitude;
    }

    if (warning != "") {
      System.out.println(warning);
    }
    return speed;
  }

  /**
   * @param speed Set the PWM rate of the bridge motor.
   */
  public void setMotor(double speed) {
    mMotor.setSpeed(mLimitSpeed(speed));
  }

  /**
   * Stops the bridge motor.
   */
  public void stop() {
    setMotor(0.0);
    // SmartDashboard.putNumber("BRIDGE STOP TIMESTAMP", Timer.getFPGATimestamp());
  }

  /**
   * Set the bridge motor to raise the bridge.
   */
  public void setRaise() {
    mMotor.setSpeed(RobotMap.BRIDGE_RAISEPWM);
  }

  /**
   * Set the bridge motor to lower the bridge.
   */
  public void setLower() {
    mMotor.setSpeed(RobotMap.BRIDGE_LOWERPWM);
  }

  /**
   * @return Returns true if the bridge motor PWM rate is not zero.
   */
  public boolean isPWMMoving() {
    return mMotor.getSpeed() != 0.0;
  }

  /**
   * @return The PWM rate of the bridge motor.
   */
  public double getSpeed() {
    return mMotor.getSpeed();
  }

  public Encoder getEncoder() {
    return mBridgeEncoder;
  }

  /**
   * @return The current flowing through the bridge motor controller.
   */
  public double getCurrent() {
    return PowerDistributionPanelReporter.getChannelCurrent(RobotMap.PDP_BRIDGE);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new BridgeDriver());
  }

}

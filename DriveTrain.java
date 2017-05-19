package org.usfirst.frc.team6485.robot.subsystems;

import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.commands.DriveTrainDriver;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Drive Train subsystem.<br>
 * <br>
 * Forwards: 1, Back: -1<br>
 * Left: 1, Right: -1
 * 
 * @author Kyle Saburao
 */
public class DriveTrain extends Subsystem {

  private Spark mFrontLeftController, mRearLeftController, mFrontRightController,
      mRearRightController;
  private RobotDrive mEngine;
  private Encoder mDriveEncoder;
  private ADXRS450_Gyro mDriveGyroscope;

  private double mAutonomousEncoderDistance;
  private final double kDriveTrainPWMMagnitudeLimit = RobotMap.DRIVETRAIN_PWMLIMIT;

  // Initialize drive train
  public DriveTrain() {

    // Create motor controller objects.
    mFrontLeftController = new Spark(RobotMap.FRONT_LEFT_MOTOR);
    mRearLeftController = new Spark(RobotMap.REAR_LEFT_MOTOR);
    mFrontRightController = new Spark(RobotMap.FRONT_RIGHT_MOTOR);
    mRearRightController = new Spark(RobotMap.REAR_RIGHT_MOTOR);

    // Create "engine" object with motor controller objects. ORDER IS IMPORTANT.
    mEngine = new RobotDrive(mFrontLeftController, mRearLeftController, mFrontRightController,
        mRearRightController);

    // Create Drive Train encoder placed on left side.
    mDriveEncoder = new Encoder(0, 1, true, EncodingType.k4X);

    // Create gyroscope object.
    mDriveGyroscope = new ADXRS450_Gyro();

    // Drive Train initialization parameters.
    mEngine.setSafetyEnabled(true);
    mEngine.setExpiration(0.300);
    mEngine.setMaxOutput(1.00);
    mEngine.setSensitivity(1.00);

    // Encoder initialization parameters.
    mDriveEncoder.setDistancePerPulse(RobotMap.DRIVETRAIN_WHEELCIRCUMFERENCEMETRES / 360.0);
    mDriveEncoder.setSamplesToAverage(10);
    mDriveEncoder.setMaxPeriod(0.075);

    mAutonomousEncoderDistance = 0.0;

  }

  public void setAutonomousEncoderDistance(double distance) {
    mAutonomousEncoderDistance = distance;
  }

  public double getAutonomousEncoderDistance() {
    return mAutonomousEncoderDistance;
  }

  private double limit(double num) {
    if (num > kDriveTrainPWMMagnitudeLimit) {
      num = kDriveTrainPWMMagnitudeLimit;
    } else if (num < -kDriveTrainPWMMagnitudeLimit) {
      num = -kDriveTrainPWMMagnitudeLimit;
    }
    return num;
  }

  /**
   * Standard tank drive controls.<br>
   * Bad arguments outside of [-.95, .95] are truncated down to the limits. No scaling is preserved.
   * 
   * @param leftStick Left Motor Group request
   * @param rightStick Right Motor Group request
   */
  public void tankDrive(double leftStick, double rightStick) {
    mEngine.tankDrive(limit(leftStick), limit(rightStick));
  }

  /**
   * Standard arcade drive controls.<br>
   * Bad arguments outside of [-.95, .95] are truncated down to the limits. No scaling is preserved.
   * 
   * @param leftStick Forward and backwards request
   * @param rightStick Turning request
   */
  public void arcadeDrive(double leftStick, double rightStick) {
    mEngine.arcadeDrive(limit(leftStick), limit(rightStick));
  }

  public void drive(double speed, double curve) {
    mEngine.drive(speed, curve);
  }

  /**
   * Only allows the drive train to drive back and forth. <br>
   * 
   * @param speed Obvious (1 full forward, -1 full backwards)
   */
  @Deprecated
  public void forwardBackDrive(double speed) {
    tankDrive(speed, speed);
  }

  /**
   * Only allows the drive train to conduct a point turn.
   * 
   * @param turnrate 1: Full Rotate Left, -1: Full Rotate Right
   */
  public void turnOnSpot(double turnrate) {
    tankDrive(-turnrate, turnrate);
  }

  /**
   * Brings the drive train to a full halt.
   */
  public void stop() {
    tankDrive(0, 0);
  }

  /**
   * Requests the drive train to go full-ahead at +95% PWM.
   */
  @Deprecated
  public void flankSpeed() {
    tankDrive(0.95, 0.95);
  }

  /**
   * @return The gyroscope of the drive train.
   */
  public ADXRS450_Gyro getGyro() {
    return mDriveGyroscope;
  }

  /**
   * 
   * @param index The PWM index of the motor.
   * @return The motor PWM Rate of motor[i]
   */
  public double getMotorPWM(int index) {
    switch (index) {
      case 0:
        return mFrontLeftController.getSpeed();
      case 1:
        return mRearLeftController.getSpeed();
      case 2:
        return mFrontRightController.getSpeed();
      case 3:
        return mRearRightController.getSpeed();

      // Required piece that does nothing at all.
      default:
        return mFrontLeftController.getSpeed();
    }
  }

  /**
   * 
   * @return All of the motors PWM values as an array of 4 floats.
   */
  public double[] getMotorPWMS() {
    double[] motorArray = new double[4];

    motorArray[0] = mFrontLeftController.getSpeed();
    motorArray[1] = mRearLeftController.getSpeed();
    motorArray[2] = mFrontRightController.getSpeed();
    motorArray[3] = mRearRightController.getSpeed();

    return motorArray;
  }

  public Encoder getEncoder() {
    return mDriveEncoder;
  }

  @Override
  public void initDefaultCommand() {
    // Default command is operator control over drive system
    setDefaultCommand(new DriveTrainDriver());
  }

}

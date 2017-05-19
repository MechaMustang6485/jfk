package org.usfirst.frc.team6485.robot.subsystems;

import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.commands.ClimberDriver;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 90:1 CIM Climbing Motor
 * 
 * @author Kyle Saburao
 */
public class Climber extends Subsystem {

  private Spark mMotor;

  public Climber() {
    mMotor = new Spark(RobotMap.CLIMB_MOTOR);
  }

  public void setSpeed(double speed) {
    mMotor.setSpeed(speed);
  }

  public void stop() {
    mMotor.setSpeed(0.0);
  }

  public double getPWMRate() {
    return mMotor.getSpeed();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ClimberDriver());
  }
}


package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.subsystems.Climber;
import org.usfirst.frc.team6485.robot.utility.XBOX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Kyle Saburao
 */
public class ClimberDriver extends Command {

  private double mControllerRequest;
  private final double kMinimumSpeedMagnitude = 0.05;
  private Climber mSystem;
  private XBOX mController;

  public ClimberDriver() {
    requires(Robot.CLIMBER);
    mSystem = Robot.CLIMBER;
    mController = Robot.OI.getXBOX();
    setInterruptible(true);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    mSystem.stop();
  }

  private void analogControl() {
    mControllerRequest =
        Math.copySign(mController.getLeftTrigger(), RobotMap.CLIMBER_MAGNITUDE_SIGN);
    if (Math.abs(mControllerRequest) > kMinimumSpeedMagnitude) {
      mSystem.setSpeed(mControllerRequest);
    } else {
      mSystem.stop();
    }

    SmartDashboard.putNumber("Climber PWM", mSystem.getPWMRate());
    SmartDashboard.putNumber("Climber Trigger", mControllerRequest);
  }

  private void monoControl() {
    mSystem.setSpeed(-0.60);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (mController.getButton(5)) {
      monoControl();
    } else {
      analogControl();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    mSystem.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

}

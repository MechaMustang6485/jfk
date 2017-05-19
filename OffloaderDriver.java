package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;
import org.usfirst.frc.team6485.robot.subsystems.Offloader;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Kyle Saburao
 */
public class OffloaderDriver extends Command {

  private double mXYAxisRequestL;
  private Offloader mOffloader;

  /**
   * Control the offloader motor using the left stick of the Xbox controller. <br>
   * Up rolls (tightens) the fabric to release fuel, while down unrolls the fabric to hold them.
   * <br>
   * <br>
   * <b>BE SURE TO MAKE SURE THAT THE MOTOR DOESN'T OVER GO THE LIMITS. DO NOT OVERTIGHTEN OR
   * OVERLOOSEN.</b>
   */
  public OffloaderDriver() {
    requires(Robot.OFFLOADER);
    mOffloader = Robot.OFFLOADER;
    setInterruptible(true);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.OFFLOADER.stop();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // Negative request will make the vinyl taut while positive will unroll it.
    mXYAxisRequestL = Robot.OI.getXBOX().getLeftJoyY();
    if (Math.abs(mXYAxisRequestL) > 0.075) {
      mOffloader.set(mXYAxisRequestL);
    } else {
      mOffloader.stop();
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
    mOffloader.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}

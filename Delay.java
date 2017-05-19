package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Delays a command group for a specified amount of seconds.<br>
 * 60 seconds maximum.
 * 
 * @author Kyle Saburao
 */
public class Delay extends Command {

  private double mTimeLength;

  /**
   * Analogous to a wait function.
   * 
   * @param seconds The time to halt the entire system.
   */
  public Delay(double seconds) {
    mTimeLength = seconds;
    setTimeout(seconds + .150);
    setInterruptible(false);
    requires(Robot.DRIVETRAIN);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.DRIVETRAIN.stop();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.DRIVETRAIN.stop(); // To satisfy the watchdog.
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (timeSinceInitialized() >= mTimeLength) || isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.DRIVETRAIN.stop();
    System.out.println(String.format("Ending delay of %.3f seconds after %.3f actual seconds.",
        mTimeLength, (timeSinceInitialized())));
  }

}

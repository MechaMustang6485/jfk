package org.usfirst.frc.team6485.robot.commands;

import org.usfirst.frc.team6485.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * @author Kyle Sburao
 */
public class StopBridge extends InstantCommand {

  public StopBridge() {
    super();
    requires(Robot.BRIDGE);
  }

  // Called once when the command executes
  @Override
  protected void initialize() {
    Robot.BRIDGE.stop();
  }

}

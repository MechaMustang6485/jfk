package org.usfirst.frc.team6485.robot.commandgroups;

import org.usfirst.frc.team6485.robot.RobotMap;
import org.usfirst.frc.team6485.robot.commands.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author Kyle Saburao
 */
public class CG_PassBaseLine extends CommandGroup {

  public CG_PassBaseLine() {
    addSequential(new DriveDistance(RobotMap.BASELINE_METRES, 0.62));
  }

}

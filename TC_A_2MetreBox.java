package org.usfirst.frc.team6485.robot.autonomous;

import org.usfirst.frc.team6485.robot.commands.DriveDistance;
import org.usfirst.frc.team6485.robot.commands.DriveTrainAutoTurn;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author Kyle Saburao
 */
public class TC_A_2MetreBox extends CommandGroup {

  /**
   * Drive a 2 metre box perimeter and return to the original position.
   */
  public TC_A_2MetreBox() {

    addSequential(new DriveDistance(2.0, 0.57));
    addSequential(new DriveTrainAutoTurn(90.0));
    addSequential(new DriveDistance(2.0, 0.57));
    addSequential(new DriveTrainAutoTurn(90.0));
    addSequential(new DriveDistance(2.0, 0.57));
    addSequential(new DriveTrainAutoTurn(90.0));
    addSequential(new DriveDistance(2.0, 0.57));
    addSequential(new DriveTrainAutoTurn(90.0));

  }
}

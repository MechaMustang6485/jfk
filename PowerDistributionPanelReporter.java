package org.usfirst.frc.team6485.robot.utility;

import org.usfirst.frc.team6485.robot.RobotMap;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Kyle Saburao
 */
public class PowerDistributionPanelReporter {

  private static PowerDistributionPanel PDP = new PowerDistributionPanel();

  public static double getChannelCurrent(int index) {
    return PDP.getCurrent(index);
  }

  private static void reportChannels() {
    SmartDashboard.putNumber("FL Current", getChannelCurrent(RobotMap.PDP_FL));
    SmartDashboard.putNumber("FR Current", getChannelCurrent(RobotMap.PDP_FR));
    SmartDashboard.putNumber("RL Current", getChannelCurrent(RobotMap.PDP_RL));
    SmartDashboard.putNumber("RR Current", getChannelCurrent(RobotMap.PDP_RR));
    SmartDashboard.putNumber("Bridge Current", getChannelCurrent(RobotMap.PDP_BRIDGE));
    SmartDashboard.putNumber("Offloader Current", getChannelCurrent(RobotMap.PDP_OFFLOADER));
  }

  public static void report() {
    reportChannels();
  }

}

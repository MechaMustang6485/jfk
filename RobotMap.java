package org.usfirst.frc.team6485.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name.
 * This provides flexibility changing wiring, makes checking the wiring easier and significantly
 * reduces the number of magic numbers floating around.<br>
 * 
 * @author Kyle Saburao
 */
public class RobotMap {

  public static final int FRONT_LEFT_MOTOR = 0, REAR_LEFT_MOTOR = 1, FRONT_RIGHT_MOTOR = 2,
      REAR_RIGHT_MOTOR = 3; // Good

  public static final int CLIMB_MOTOR = 7; // Good

  public static final int BRIDGE_MOTOR = 5; // Good

  public static final int OFFLOADER_MOTOR = 4; // Good

  public static final int PDP_FL = 0, PDP_FR = 15, PDP_RL = 1, PDP_RR = 14; // Good

  // FIX THESE
  public static final int PDP_BRIDGE = 11, PDP_OFFLOADER = 6, PDP_CLIMB = 2; // Good

  public static final int LOGITECH_PORT = 0, XBOX_PORT = 1; // Good

  // Constants sheet

  // 95% because full motor speed would saturate the motors and prevent normal turning via
  // increasing one side's motors.
  public static final double DRIVETRAIN_PWMLIMIT = 0.95;

  public static final double BRIDGE_IDLECURRENTMAGNITUDE = 0.1;

  public static final double BRIDGE_NORMALPWM = 0.20; // Positive raises the bridge

  public static final double BRIDGE_RAISEPWM = 0.24, BRIDGE_LOWERPWM = -0.20,
      BRIDGE_MAXSAFEPWM = 0.26;

  public static final double OFFLOADER_MAXSAFEPWM = 1.00;

  public static final double OFFLOADER_IDLECURRENT = 1.375, OFFLOADER_MAXWORKINGCURRENT = 1.5,
      OFFLOADER_STALLCURRENT = 5.0;

  public static final double OFFLOADER_STALLTIME = 2.0;

  public static final double DRIVETRAIN_WHEELCIRCUMFERENCEMETRES = Math.PI * 0.1524;

  public static final double DRIVEDISTANCE_MINIMUMALLOWABLEPWMMAGNITUDE = 0.40;

  public static final double DRIVEDISTANCE_RAMPINGDISTANCEMETRES = 0.15;

  public static final double AUTODRIVE_GYROKP = 0.07; // Try 0.06

  public static final double DRIVETIMED_RAMPPERIODSECONDS = 0.50;

  public static final double CLIMBER_MAGNITUDE_SIGN = -1.0; // NEGATIVE

  // ENUMS

  public enum RUNNING_MODE {
    DISABLED, TELEOP, AUTO
  }

  public enum OFFLOADER_STATE {
    TAUT, FREE
  }

  // MATCH CONSTANTS

  public static final double BASELINE_METRES = 2.45;

  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

}

package org.usfirst.frc.team6485.robot;

import org.usfirst.frc.team6485.robot.RobotMap.RUNNING_MODE;
import org.usfirst.frc.team6485.robot.commandgroups.CG_PassBaseLine;
import org.usfirst.frc.team6485.robot.subsystems.Bridge;
import org.usfirst.frc.team6485.robot.subsystems.Climber;
import org.usfirst.frc.team6485.robot.subsystems.DriveTrain;
import org.usfirst.frc.team6485.robot.subsystems.Offloader;
import org.usfirst.frc.team6485.robot.utility.BridgeReporter;
import org.usfirst.frc.team6485.robot.utility.DriveTrainReporter;
import org.usfirst.frc.team6485.robot.utility.OffloaderReporter;
import org.usfirst.frc.team6485.robot.utility.PowerDistributionPanelReporter;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * CG_: Command Group <br>
 * TC_: Test case <br>
 * A_: Autonomous command group
 * 
 * @author Kyle Saburao
 */
public class Robot extends IterativeRobot {

  public static OI OI;
  public static DriveTrain DRIVETRAIN;
  public static Climber CLIMBER;
  public static Bridge BRIDGE;
  public static Offloader OFFLOADER;
  public static DriverStation DRIVERSTATION;
  public static CameraServer CAMERASERVER;
  public static UsbCamera CAMERA;
  public static Alliance ALLIANCECOLOUR;
  public static boolean FMS_CONNECTED;

  public static RUNNING_MODE robotMode;

  Command autonomousCommand;
  SendableChooser<Command> chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    DRIVETRAIN = new DriveTrain();
    CLIMBER = new Climber();
    BRIDGE = new Bridge();
    OFFLOADER = new Offloader();

    OI = new OI();

    DRIVERSTATION = DriverStation.getInstance();
    CAMERASERVER = CameraServer.getInstance();
    ALLIANCECOLOUR = DRIVERSTATION.getAlliance();
    FMS_CONNECTED = DRIVERSTATION.isFMSAttached();

    chooser.addDefault("Pass Baseline", new CG_PassBaseLine());

    SmartDashboard.putData("Auto Mode", chooser);
    SmartDashboard.putData("Drive Train", DRIVETRAIN);
    SmartDashboard.putData("Bridge", BRIDGE);
    SmartDashboard.putData("Offloader", OFFLOADER);
    SmartDashboard.putData("Climber", CLIMBER);

    CAMERA = CAMERASERVER.startAutomaticCapture();
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You can use it to reset
   * any subsystem information you want to clear when the robot is disabled.
   */
  @Override
  public void disabledInit() {
    // Stop the subsystem motors.
    Robot.DRIVETRAIN.stop();
    Robot.BRIDGE.stop();
    Robot.OFFLOADER.stop();
    Robot.CLIMBER.stop();

    // Cancel all queued commands.
    Scheduler.getInstance().removeAll();
    robotMode = RUNNING_MODE.DISABLED;

    // Reset encoders
    Robot.DRIVETRAIN.getEncoder().reset();
    Robot.BRIDGE.getEncoder().reset();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
    Robot.DRIVETRAIN.stop();
    Robot.BRIDGE.stop();
    Robot.OFFLOADER.stop();
    Robot.CLIMBER.stop();
    report();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString code to get the auto name from the text box below the Gyro
   *
   * You can add additional auto modes by adding additional commands to the chooser code above (like
   * the commented example) or additional comparisons to the switch structure below with additional
   * strings & commands.
   */
  @Override
  public void autonomousInit() {
    robotMode = RUNNING_MODE.AUTO;
    autonomousCommand = chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new MyAutoCommand(); break; case
     * "Default Auto": default: autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (autonomousCommand != null) {
      autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    report();
  }

  @Override
  public void teleopInit() {
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    robotMode = RUNNING_MODE.TELEOP;

    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
  }

  /**
   * This function is called periodically during operator control
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    report();
  }

  /**
   * This function is called periodically during test mode
   */
  @Override
  public void testPeriodic() {
    LiveWindow.run();
  }

  public void report() {
    String robotmode_string = "";
    switch (robotMode) {
      case DISABLED:
        robotmode_string = "DISABLED";
        break;
      case TELEOP:
        robotmode_string = "TELEOP";
        break;
      case AUTO:
        robotmode_string = "AUTO";
        break;
    }
    SmartDashboard.putString("ROBOT MODE", robotmode_string);

    // Subsystem Reporters
    DriveTrainReporter.report();
    BridgeReporter.report();
    OffloaderReporter.report();
    PowerDistributionPanelReporter.report();
  }

}

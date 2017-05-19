package org.usfirst.frc.team6485.robot.utility;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Logitech 3D Pro Extreme Helper Class <br>
 * Calling a button out of bounds will always return false.
 * 
 * @author Kyle Saburao
 *
 */
public class Logitech3DPro extends Joystick {

  private int mMinimumButtonIndex = 0;
  private int mMaximumButtonIndex = 12;

  public Logitech3DPro(int port) {
    super(port);
  }

  public double getJoyX() {
    return super.getRawAxis(0);
  }

  public double getJoyY() {
    return super.getRawAxis(1);
  }

  public double getRotation() {
    return super.getRawAxis(2);
  }

  public boolean getMainTrigger() {
    return super.getRawButton(1);
  }

  public double getSlider() {
    return super.getRawAxis(3);
  }

  /**
   * Return a double with range [0 - 1] on the slider scale with 0 at full drop and 1 at full
   * raise.<br>
   * The slider has a rounding zone of 5% on either end.
   */
  public double getSliderScale() {
    double scale = 0.5 * (-super.getRawAxis(3) + 1.0);
    if (scale > 0.95)
      scale = 1;
    if (scale < 0.05)
      scale = 0;

    return scale;
  }

  /**
   * 
   * @param index Button index from [0, 12]
   * @return State of button or false if out of bounds.
   */
  public boolean getButton(int index) {
    if (index < mMinimumButtonIndex || index > mMaximumButtonIndex) {
      return false;
    }
    return super.getRawButton(index);
  }

}

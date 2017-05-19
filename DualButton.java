package org.usfirst.frc.team6485.robot.utility;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * @author Kyle Saburao
 */
public class DualButton extends Button {

  private JoystickButton mB1, mB2;

  public DualButton(JoystickButton Button1, JoystickButton Button2) {
    mB1 = Button1;
    mB2 = Button2;
  }

  @Override
  public boolean get() {
    return mB1.get() && mB2.get();
  }

}

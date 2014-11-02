package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DescriptionPanel extends JPanel {
  
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  
  public DescriptionPanel() {
    setBackground(BACKGROUND_COLOR);
    JLabel title = new JLabel("Status:");
    add(title);
  }

  public void status(String string) {
    JLabel label = new JLabel(string);
    add(label);
  }
}

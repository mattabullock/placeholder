package view;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Server;

public class ActionPanel extends JPanel {
  
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  
  public ActionPanel(final Server server) {
    setBackground(BACKGROUND_COLOR);
    JLabel title = new JLabel("Actions");
    JButton screenshot = new JButton("Take screenshot");
    screenshot.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand();
      }
      
    });   
    
    JButton passwords = new JButton("Gather passwords");
    passwords.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand();
      }
    });
    
    JButton keylog = new JButton("Log keystrokes");
    keylog.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand();
      }
    });
    
    add(title);
    add(screenshot);
    add(passwords);
    add(keylog);
  }

}

package view;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DescriptionPanel extends JPanel {

  private static final long serialVersionUID = -7569703555141749771L;
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  
  // in points
  private static final int FONT_SIZE = 18;
  
  private JLabel numClientsConnected;
  private JLabel numClientsSelected;

  
  public DescriptionPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(BACKGROUND_COLOR);
    JLabel title = new JLabel("Status:");
    title.setFont(new Font(null, 0, FONT_SIZE * 3 / 2));
    numClientsConnected = new JLabel("0 clients connected");
    numClientsConnected.setFont(new Font(null, 0, FONT_SIZE));
    numClientsSelected = new JLabel("0 clients selected");
    numClientsSelected.setFont(new Font(null, 0, FONT_SIZE));


    add(title);
    add(numClientsConnected);
    add(numClientsSelected);

  }

  public void status(String string) {
    JLabel label = new JLabel(string);
    add(label);
  }

  public void numClients(int size) {
    if (size == 1) {
      numClientsConnected.setText("1 client connected");
    } else {
      numClientsConnected.setText(size + " clients connected");
    }
  }

  public void numSelectedClients(int size) {
    if (size == 1) {
      numClientsSelected.setText("1 client selected");
    } else {
      numClientsSelected.setText(size + " clients selected");
    }
  }
}

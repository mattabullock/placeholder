package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Server;

public class GUI extends JFrame {
  
  // in pixels
  private static final int DIVIDER_SIZE = 3;
  private static final int FRAME_WIDTH = 800;
  private static final int FRAME_HEIGHT = 600;
  
  // ratio
  private static final double DESCRIPTION_PANEL_WIDTH = 0.4;
  
  private final Server server;
  
  public GUI(Server server) {
    this.server = server;
    
    setBackground(Color.GREEN);
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    setTitle("Placeholder Control");
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    JMenuBar menuBar = new MenuBar();
    setJMenuBar(menuBar);
    
    JPanel descriptionPanel = new DescriptionPanel();
    JPanel actionPanel = new ActionPanel(server);
    JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descriptionPanel, actionPanel);
    mainPanel.setDividerSize(DIVIDER_SIZE);
    JPanel consolePanel = new ConsolePanel();

    getContentPane().add(mainPanel);
    mainPanel.setVisible(true);
    mainPanel.setResizeWeight(DESCRIPTION_PANEL_WIDTH);

    getContentPane().add(consolePanel);
  }

}

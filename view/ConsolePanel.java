package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


public class ConsolePanel extends JPanel {
  
  private static final Color BACKGROUND_COLOR = Color.BLACK;
  private static final Color TEXT_COLOR = Color.WHITE;
  
  public ConsolePanel() {
    setLayout(new BorderLayout());
    setBackground(BACKGROUND_COLOR);
    JTextPane console = new JTextPane();
    console.setCaretColor(TEXT_COLOR);
    // TODO: set styles and such
    // http://stackoverflow.com/questions/4059198/jtextpane-appending-a-new-string
    JScrollPane scroll = new JScrollPane(console);
    console.setForeground(TEXT_COLOR);
    console.setBackground(BACKGROUND_COLOR);
    console.setText("$ ");
    
    add(scroll);
  }

}

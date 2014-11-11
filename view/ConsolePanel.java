package view;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class ConsolePanel extends JPanel {

  private static final long serialVersionUID = 4088768537786851256L;
  private static final Color BACKGROUND_COLOR = Color.BLACK;
  public static final Color TEXT_COLOR = Color.WHITE;
  private final JTextPane console;

  public ConsolePanel() {
    setLayout(new BorderLayout());
    setBackground(BACKGROUND_COLOR);
    console = new JTextPane();
    console.setCaretColor(TEXT_COLOR);
    JScrollPane scroll = new JScrollPane(console);
    console.setForeground(TEXT_COLOR);
    console.setBackground(BACKGROUND_COLOR);
    console.setText("");

    add(scroll);
  }

  public void message(String message, Color color) {

    StyledDocument doc = console.getStyledDocument();

    // color message
    SimpleAttributeSet sas = new SimpleAttributeSet();
    StyleConstants.setForeground(sas, color);

    try {
      doc.insertString(doc.getLength(), message + System.getProperty("line.separator"), sas);
    } catch(Exception e) { 
      e.printStackTrace();
    }
  }
}

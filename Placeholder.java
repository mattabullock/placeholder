import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Server;

public class Placeholder {
  public static void main(String[] args) {
    
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    new Server();
  }
}
package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


public class MenuBar extends JMenuBar {

  private static final long serialVersionUID = 3072752201438498748L;
  private static JMenu commandMenu, computerMenu;

  public MenuBar() {
    commandMenu = new JMenu("Commands");
    computerMenu = new JMenu("Computers");
    
    commandMenu.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println(e);
      }
    });
    
    computerMenu.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println(e);
      }
    });
    
    add(commandMenu);

    add(computerMenu);
  }

}

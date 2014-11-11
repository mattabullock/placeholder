package view;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

  private static final long serialVersionUID = 3072752201438498748L;
  private JMenu commandMenu, computerMenu;


  public MenuBar(final GUI gui) {
//    commandMenu = new JMenu("Commands");
    computerMenu = new JMenu("Computers");
    
//    commandMenu.addMouseListener(new MouseAdapter() {
//      @Override
//      public void mouseClicked(MouseEvent e) {
//        System.out.println(e);
//      }
//    });
    
//    computerMenu.addMouseListener(new MouseAdapter() {
//      @Override
//      public void mouseClicked(MouseEvent e) {
//        System.out.println("menu");
//      }
//    });
    
    JMenuItem selectComputers = new JMenuItem("Select computers");
    selectComputers.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gui.setLeft(0);
      }
    });
        
    JMenuItem viewComputers = new JMenuItem("View information about current computers");
    viewComputers.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gui.setLeft(1);
      }
    });
    
    computerMenu.add(selectComputers);
    computerMenu.add(viewComputers);
    
//    add(commandMenu);

    add(computerMenu);
  }

}

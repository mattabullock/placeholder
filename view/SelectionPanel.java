package view;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectionPanel extends JPanel {

  private static final long serialVersionUID = 5249320877761722353L;
  private static final int FONT_SIZE = 18;

  private static JList<InetAddress> ipJList;
  private final Set<InetAddress> ips; 
  
  public SelectionPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Select IPs");
    title.setFont(new Font(null, 0, FONT_SIZE * 3 / 2));
    ipJList = new JList<InetAddress>();
    JScrollPane scroll = new JScrollPane(ipJList);
    
    add(title);
    add(scroll);
    ips = new HashSet<InetAddress>();
    
    Set<InetAddress> set = new HashSet<InetAddress>();
    try {
      set.add(InetAddress.getByName("12.34.56.78"));
      set.add(InetAddress.getByName("1.34.56.78"));
      set.add(InetAddress.getByName("12.3.56.78"));
      set.add(InetAddress.getByName("12.34.5.78"));
      set.add(InetAddress.getByName("12.34.56.7"));

    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    updateIpSet(set);
  }
  
  public void updateIpSet(Set<InetAddress> newIps) {
    for (InetAddress ip : newIps) {
      if (!ips.contains(ip)) {
        ips.add(ip);
      } 
    }

    Iterator<InetAddress> i = ips.iterator();
    
    while (i.hasNext()) {
      if (!newIps.contains(i.next())) {
        i.remove();
      }
    }
    
    ipJList.setListData(ips.toArray(new InetAddress[]{}));
  }

  public static Set<InetAddress> getSelectedClients() {
    Set<InetAddress> selectedClients = new HashSet<InetAddress>();
    
    for (int i : ipJList.getSelectedIndices()) {
      selectedClients.add(ipJList.getModel().getElementAt(i));
    }
    return selectedClients;
  }
}

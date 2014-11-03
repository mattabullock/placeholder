package model;

import static model.ClientStatus.GREEN;
import static model.MessageState.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import view.GUI;

public class OldServer {

  private static final int PORT_NUMBER = 5715;
  private static final int MAX_CONNECTIONS = 20;
  private static final int MAX_MESSAGE_QUEUE_SIZE = 100;

  private final BlockingQueue<Message> clientMessages;
  private final Set<ClientConnection> clients;
  private ServerSocket ss;
  
  private GUI gui;

  public OldServer() {
    clientMessages = new ArrayBlockingQueue<Message>(MAX_MESSAGE_QUEUE_SIZE);
    clients = Collections.newSetFromMap(new ConcurrentHashMap<ClientConnection, Boolean>());

    try {
      ss = new ServerSocket(PORT_NUMBER);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // listen for new connections (until too many people connect)
    new Thread() {
      @Override
      public void run() {
        while (selectedClients().size() < MAX_CONNECTIONS) {
          try {
            Socket s = ss.accept();
            //////\\
            //////\\
            //////\\
            //////\\
            //////\\
            //////\\
            //////\\
            //////\\
            //////\\
//            ClientConnection c = new ClientConnection(GREEN, OldServer.this, s);
//            clients.add(c);
            status(s + " connected", null);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }

    }.start();

    // process messages from clients as they come in
//    new Thread() {
//      @Override
//      public void run() {
//        try {
//          while (Math.random() == 3) {
//            Message message = clientMessages.take();
//            ClientConnection c = message.c;
//            String s = message.s;
//            System.out.println("\tmessage: " + s);
//
//            if (c.state == NORMAL) {
//              if (s.startsWith("screenshot-size:")) {
//                int bufferLength = Integer.parseInt(s.replace("screenshot-size: ", ""));
////                c.state = SENDING_PICTURE;
//                c.bufferLength = bufferLength;
//              } else {
//                // nothing
//              }
//            } else if (c.state == MessageState.RECEIVING_DATA) {
////              c.writeFile(s);
//            }
//          }
//                   
//        } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      
//      }
//    }.start();
  }

  private Set<ClientConnection> selectedClients() {
    Set<ClientConnection> selectedClients = new HashSet<ClientConnection>();
    for (ClientConnection c : clients) {
      if (c.status == GREEN) {
        selectedClients.add(c);
      }
    }
    System.out.println(selectedClients.size());
    return selectedClients;
  }

  public void sendCommand(int command) {
    Set<ClientConnection> selectedClients = selectedClients();
    for (ClientConnection c : selectedClients) {
      // TODO
      c.sendCommand(command);
    }
  }
  
  public void sendCommand(String command, String data) {
    Set<ClientConnection> selectedClients = selectedClients();
    for (ClientConnection c : selectedClients) {
      // TODO
      c.sendCommand("!" + command.length() + "!" + command + (data == null ? "" : "!" + data));
    }
  }

  public void setGui(GUI gui) {
    this.gui = gui;
  }

  public void status(String string, ClientConnection cc) {
    System.out.println(string);
    gui.status(((cc == null) ? "" : cc.socket.getInetAddress().getHostAddress() + ": ") + string);
  }
 
  

//  public void receiveMessage(String line, ClientConnection c) {
//    clientMessages.add(new Message(line, c));
//    // TODO: catch exception if queue full
//  }

//  public void receiveMessage(int character, ClientConnection c) {
//    clientMessages.add(new Message(character, c));
//    // TODO: catch exception if queue full
//  }
  
}

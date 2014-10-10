package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Collections;

import static model.ClientStatus.*;

public class Server {

  private static final int PORT_NUMBER = 5715;
  private static final int MAX_CONNECTIONS = 10;
  private static final int MAX_MESSAGE_QUEUE_SIZE = 100;
  // seconds to wait to try to insert client message into message queue before
  // failing
  private static final int WAITING_TIME = 10;

  private final BlockingQueue<String> clientMessages;
  private final Set<ClientConnection> clients;
  private ServerSocket ss;

  public Server() {
    clientMessages = new ArrayBlockingQueue<String>(MAX_MESSAGE_QUEUE_SIZE);
    clients = Collections.newSetFromMap(new ConcurrentHashMap<ClientConnection, Boolean>());

    try {
      ss = new ServerSocket(PORT_NUMBER);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // listen for new connections (until too many people connect)
    // TODO: if we hit MAX_CONNECTIONS, and then drop some connections,
    // start listening again
    new Thread() {
      @Override
      public void run() {
        while (selectedClients().size() < MAX_CONNECTIONS) {
          try {
            Socket s = ss.accept();
            ClientConnection c = new ClientConnection(GREEN, Server.this, s);
            clients.add(c);
            System.out.println(s + " connected");
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }

    }.start();

    // process messages from clients as they come in
    new Thread() {
      @Override
      public void run() {
        while (true) {
          try {
            String message = clientMessages.take();
            System.out.println(message);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }.start();
  }

  private Set<ClientConnection> selectedClients() {
    Set<ClientConnection> selectedClients = new HashSet<ClientConnection>();
    for (ClientConnection c : clients) {
      if (c.status == GREEN) {
        selectedClients.add(c);
      }
    }
    return selectedClients;
  }

  public void sendCommand() {
    Set<ClientConnection> selectedClients = selectedClients();
    for (ClientConnection c : selectedClients) {
      // TODO
    }
  }

  public void receiveMessage(String line) {
    clientMessages.add(line);
    // TODO: catch exception if queue full
  }

}

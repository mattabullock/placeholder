package model;

public class Message {
  public final String s;
  public final ClientConnection c;
  public Message(String s, ClientConnection c) {
    this.s = s;
    this.c = c;
  }
}

package model;

/*
 * RED - offline
 * YELLOW - online within 30 minutes (TODO) but not currently selected
 * GREEN - online and selected to be used for commands
 */
public enum ClientStatus {
  RED, YELLOW, GREEN
}
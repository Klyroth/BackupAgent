package net.klyroth.backupagent.command;

public interface CommandSender {
    void sendMessage(String message);
    String getName();
}

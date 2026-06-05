package net.klyroth.backupagent.command;

public final class ConsoleCommandSender implements CommandSender {
    @Override public void sendMessage(String message) { System.out.println(message); }
    @Override public String getName() { return "console"; }
}

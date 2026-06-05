package net.klyroth.backupagent.command;

public interface Command {
    void execute(CommandSender sender, CommandContext context, String[] args);
}

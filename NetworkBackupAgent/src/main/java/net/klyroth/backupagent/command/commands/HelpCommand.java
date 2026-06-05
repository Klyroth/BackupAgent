package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;

public final class HelpCommand implements Command {
    private final CommandManager commandManager;
    public HelpCommand(CommandManager commandManager) { this.commandManager = commandManager; }
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        sender.sendMessage("Verfügbare Befehle:");
        commandManager.getCommands().keySet().forEach(name -> sender.sendMessage("  - " + name));
        sender.sendMessage("Nutzung: backup | status | search <query> | sort newest|oldest|size | delete <name>|oldest|latest | config | plugins | reload | update | stop");
    }
}

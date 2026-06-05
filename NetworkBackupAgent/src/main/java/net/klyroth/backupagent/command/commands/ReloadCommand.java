package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;

public final class ReloadCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        try {
            context.configService().load();
            sender.sendMessage("[OK] Config ist gültig. Für Scheduler-Änderungen bitte Agent neu starten.");
        } catch (Exception e) { sender.sendMessage("[ERROR] Reload fehlgeschlagen: " + e.getMessage()); }
    }
}

package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;

public final class UpdateCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        try {
            context.updateService().update();
            sender.sendMessage("[OK] Update installiert. Starte den Agent bei Bedarf neu.");
        } catch (Exception e) { sender.sendMessage("[ERROR] Update fehlgeschlagen: " + e.getMessage()); }
    }
}

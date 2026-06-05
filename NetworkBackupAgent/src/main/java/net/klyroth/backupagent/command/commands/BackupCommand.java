package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.backup.*;
import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.util.FileSizeFormatter;

public final class BackupCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        sender.sendMessage("[BACKUP] Manuelles Backup wird gestartet...");
        BackupResult result = context.backupService().createBackup(new BackupRequest("manual-command", sender.getName()));
        if (result.success()) sender.sendMessage("[OK] " + result.backupName() + " | " + FileSizeFormatter.format(result.sizeBytes()) + " | " + result.duration().toSeconds() + "s");
        else sender.sendMessage("[ERROR] " + result.message());
    }
}

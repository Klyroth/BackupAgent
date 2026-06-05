package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.backup.BackupInfo;
import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.config.BackupAgentConfig;
import net.klyroth.backupagent.util.DateTimeUtil;
import net.klyroth.backupagent.util.FileSizeFormatter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class SearchCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        if (args.length == 0) { sender.sendMessage("Nutzung: search <query>"); return; }
        try {
            BackupAgentConfig config = context.configService().load();
            String query = String.join(" ", args);
            List<BackupInfo> results = context.backupFileScanner().search(Path.of(config.backup().outputFolder()).toAbsolutePath(), query);
            if (results.isEmpty()) { sender.sendMessage("Keine Backups gefunden."); return; }
            for (BackupInfo info : results) print(sender, info);
        } catch (Exception e) { sender.sendMessage("[ERROR] Suche fehlgeschlagen: " + e.getMessage()); }
    }
    private void print(CommandSender sender, BackupInfo info) {
        sender.sendMessage("- " + info.name() + " | " + FileSizeFormatter.format(info.sizeBytes()) + " | " + DateTimeUtil.format(info.createdAt()));
    }
}

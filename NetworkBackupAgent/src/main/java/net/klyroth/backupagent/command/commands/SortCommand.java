package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.backup.BackupInfo;
import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.config.BackupAgentConfig;
import net.klyroth.backupagent.util.DateTimeUtil;
import net.klyroth.backupagent.util.FileSizeFormatter;

import java.nio.file.Path;

public final class SortCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        String mode = args.length == 0 ? "newest" : args[0];
        if (!mode.equalsIgnoreCase("newest") && !mode.equalsIgnoreCase("oldest") && !mode.equalsIgnoreCase("size")) {
            sender.sendMessage("Nutzung: sort newest|oldest|size"); return;
        }
        try {
            BackupAgentConfig config = context.configService().load();
            for (BackupInfo info : context.backupFileScanner().sort(Path.of(config.backup().outputFolder()).toAbsolutePath(), mode)) {
                sender.sendMessage("- " + info.name() + " | " + FileSizeFormatter.format(info.sizeBytes()) + " | " + DateTimeUtil.format(info.createdAt()));
            }
        } catch (Exception e) { sender.sendMessage("[ERROR] Sortierung fehlgeschlagen: " + e.getMessage()); }
    }
}

package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.config.BackupAgentConfig;
import net.klyroth.backupagent.storage.StorageReport;
import net.klyroth.backupagent.util.FileSizeFormatter;

import java.nio.file.Path;

public final class StatusCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        try {
            BackupAgentConfig config = context.configService().load();
            Path backupFolder = Path.of(config.backup().outputFolder()).toAbsolutePath();
            StorageReport report = context.storageService().getReport(backupFolder);
            sender.sendMessage("Status:");
            sender.sendMessage("  Backup läuft: " + context.backupService().isRunning());
            sender.sendMessage("  Intervall: " + config.backup().intervalMinutes() + " Minuten");
            sender.sendMessage("  Backup-Ordner: " + backupFolder);
            sender.sendMessage("  Freier Speicher: " + FileSizeFormatter.format(report.freeBytes()));
            sender.sendMessage("  Mindest frei: " + config.storage().minFreeSpaceGb() + " GB");
            sender.sendMessage("  Plugins: " + context.pluginManager().getPlugins().size());
        } catch (Exception e) {
            sender.sendMessage("[ERROR] Status konnte nicht gelesen werden: " + e.getMessage());
        }
    }
}

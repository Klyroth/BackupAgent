package net.klyroth.backupagent.command.commands;

import net.klyroth.backupagent.command.*;
import net.klyroth.backupagent.config.BackupAgentConfig;

import java.io.File;
import java.nio.file.Path;

public final class DeleteCommand implements Command {
    @Override
    public void execute(CommandSender sender, CommandContext context, String[] args) {
        if (args.length == 0) { sender.sendMessage("Nutzung: delete <name>|oldest|latest"); return; }
        try {
            BackupAgentConfig config = context.configService().load();
            Path folder = Path.of(config.backup().outputFolder()).toAbsolutePath();
            String target = String.join(" ", args);
            File file = switch (target.toLowerCase()) {
                case "oldest" -> context.backupFileScanner().findOldestFile(folder);
                case "latest" -> context.backupFileScanner().findLatestFile(folder);
                default -> folder.resolve(target).toFile();
            };
            if (file == null || !file.exists() || !file.getName().endsWith(".zip")) { sender.sendMessage("[ERROR] Backup nicht gefunden oder ungültig."); return; }
            if (!file.toPath().toAbsolutePath().normalize().startsWith(folder.normalize())) { sender.sendMessage("[ERROR] Ungültiger Pfad."); return; }
            if (file.delete()) sender.sendMessage("[OK] Backup gelöscht: " + file.getName());
            else sender.sendMessage("[ERROR] Backup konnte nicht gelöscht werden.");
        } catch (Exception e) { sender.sendMessage("[ERROR] Löschen fehlgeschlagen: " + e.getMessage()); }
    }
}
